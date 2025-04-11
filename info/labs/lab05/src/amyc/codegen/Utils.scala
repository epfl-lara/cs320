package amyc
package codegen

import amyc.ast.Identifier
import wasm.Function
import wasm.Instructions._

// Utilities for CodeGen
object Utils {

  // The index of the global variable that represents the free memory boundary
  val memoryBoundary: Int = 0
  // The index of the global variable that represents the input buffer
  val inputBuffer: Int = 1
  // # of global variables
  val globalsNo = 2

  // The default imports we will pass to a wasm Module
  val defaultImports: List[String] = List(
    "\"wasi_snapshot_preview1\" \"fd_write\" (func $fd_write (param i32 i32 i32 i32) (result i32))",
    "\"wasi_snapshot_preview1\" \"fd_read\" (func $fd_read (param i32 i32 i32 i32) (result i32))"
  )

  // We don't generate code for these functions in CodeGen (they are hard-coded here or in js wrapper)
  val builtInFunctions: Set[String] = Set(
    "Std_printString",
    "Std_digitToString",
    "Std_readInt",
    "Std_readString"
  )

  /** Utilities */
  // A globally unique name for definitions
  def fullName(owner: Identifier, df: Identifier): String = owner.name + "_" + df.name

  // Given a pointer to an ADT on the top of the stack,
  // will point at its field in index (and consume the ADT).
  // 'index' MUST be 0-based.
  def adtField(index: Int): Code = {
    Comment(s"adtField index: $index") <:> Const(4* (index + 1)) <:> Add
  }

  // Increment a local variable
  def incr(local: Int): Code = {
    GetLocal(local) <:> Const(1) <:> Add <:> SetLocal(local)
  }

  // A fresh label name
  def getFreshLabel(name: String = "label") = {
    Identifier.fresh(name).fullName
  }

  // Creates a known string constant s in memory
  def mkString(s: String): Code = {
    val size = s.length
    val padding = 4 - size % 4

    val completeS = s + 0.toChar.toString * padding

    val setChars = for ((c, ind) <- completeS.zipWithIndex.toList) yield {
      GetGlobal(memoryBoundary) <:> Const(ind) <:> Add <:>
        Const(c.toInt) <:> Store8
    }

    val setMemory =
      GetGlobal(memoryBoundary) <:> GetGlobal(memoryBoundary) <:> Const(size + padding) <:> Add <:>
        SetGlobal(memoryBoundary)

    Comment(s"mkString: $s") <:> setChars <:> setMemory
  }
  
  val stringLenImpl: Function = {
    Function("String_len", 1, false) { lh =>
      val size = lh.getFreshLocal()

      val label = getFreshLabel()
      Loop(label) <:>
        // Load current character
        GetLocal(0) <:> Load8_u <:>
        // If != 0
        If_void <:>
        // Increment pointer and size
        incr(0) <:> incr(size) <:>
        // Jump to loop
        Br(label) <:>
        Else <:>
        End <:>
        End <:>
      GetLocal(size)
    }
  }

  // Built-in implementation of concatenation
  val concatImpl: Function = {
    Function("String_concat", 2, false) { lh =>
      val ptrS = lh.getFreshLocal()
      val ptrD = lh.getFreshLocal()
      val label = getFreshLabel()

      def mkLoop: Code = {
        val label = getFreshLabel()
        Loop(label) <:>
        // Load current character
        GetLocal(ptrS) <:> Load8_u <:>
        // If != 0
        If_void <:>
        // Copy to destination
        GetLocal(ptrD) <:>
        GetLocal(ptrS) <:> Load8_u <:>
        Store8 <:>
        // Increment pointers
        incr(ptrD) <:> incr(ptrS) <:>
        // Jump to loop
        Br(label) <:>
        Else <:>
        End <:>
        End
      }

      // Instantiate ptrD to previous memory, ptrS to first string
      GetGlobal(memoryBoundary) <:>
      SetLocal(ptrD) <:>
      GetLocal(0) <:>
      SetLocal(ptrS) <:>
      // Copy first string
      mkLoop <:>
      // Set ptrS to second string
      GetLocal(1) <:>
      SetLocal(ptrS) <:>
      // Copy second string
      mkLoop <:>
      //
      // Pad with zeros until multiple of 4
      //
      Loop(label) <:>
      // Write 0
      GetLocal(ptrD) <:> Const(0) <:> Store8 <:>
      // Check if multiple of 4, + 3
      GetLocal(ptrD) <:> Const(4) <:> Rem <:>
      Const(3) <:> Eq <:>
      // If not
      If_void <:>
      Else <:>
        // Increment pointer and go back
        incr(ptrD) <:>
        Br(label) <:>
      End <:>
      End <:>
      //
      // Put string pointer to stack, set new memory boundary and return
      GetGlobal(memoryBoundary) <:> GetLocal(ptrD) <:> Const(1) <:> Add <:> SetGlobal(memoryBoundary)
    }
  }

  val digitToStringImpl: Function = {
    Function("Std_digitToString", 1, false) { lh =>
      // We know we have to create a string of total size 4 (digit code + padding), so we do it all together
      // We do not need to shift the digit due to little endian structure!
      GetGlobal(memoryBoundary) <:> GetLocal(0) <:> Const('0'.toInt) <:> Add <:> Store <:>
      // Load memory boundary to stack, then move it by 4
      GetGlobal(memoryBoundary) <:>
      GetGlobal(memoryBoundary) <:> Const(4) <:> Add <:> SetGlobal(memoryBoundary)
    }
  }

  // You don't need to understand this or printString, but know that we use the following for
  // reading and writing from stdin and to stdout in WASI.
  // https://github.com/WebAssembly/WASI/blob/main/legacy/preview1/docs.md
  val readStringImpl: Function = {
    Function("Std_readString", 0, false) { lh =>
      val bytesRead = lh.getFreshLocal()
      val ptr = lh.getFreshLocal()
      val ptrD = lh.getFreshLocal()

      // NOTE: this code assumes the input will never contain a null byte.
      // This is a decently reasonable assumption.
      // The only consequence of having a null byte is that any input after that in that read will be lost.

      // Notice that due to how fd_read *works*,
      // this might read multiple lines of user input.
      // We thus need to proceed as follows:
      // - Keep a global pointer of buffered reading
      // - Read a single line of input
      // - Shift the buffered reading pointer
      // - If no line is available, read a new buffer from the host

      // We can't read "until a certain character is encountered". So instead we read everything, then split by lines.

      // Since every line is only read once by an `fd_read` call, every further call must use the earlier read's content to get the next line. This is global state that needs to be maintained.

      // E.g. if we call Std_readString 3 times with the input
      // ```
      // 42
      // 43
      // [... given later]
      // 44
      // ```
      // 1. Std_readString -> fd_read -> obtains `42\n43` -> returns 42
      // 2. Std_readString -> fd_read already called -> returns 43
      // 3. Std_readString -> fd_read already called but empty -> fd_read -> obtains `44` -> return 44
      //
      //
      // TODO: when we reach the end of the input buffer, but a full line wasn't read
      //       this code considers the line ended. This is not correct, but is only relevant if more than 4096 bytes of
      //       input are read.

      // NOTE: must keep alignment
      val bufferSize = 4096
      assert(bufferSize % 4 == 0)

      // host_read takes a pointer and a length, and returns the number of bytes read.
      // We will read at most bufferSize bytes.
      // The pointer is the global memory boundary
      def readFromHost = {
        val label = getFreshLabel()
        // We want to call fd_read. We need to set the iovec structure.
        // We will read up to bufferSize bytes.
        // 1) Set the pointer to the buffer in a definitely-free position
        // 2) Set the length
        // 3) Call fd_read
        // 4) Check the return value (if an error occurred, we trap)
        // 5) Get the number of bytes read
        // 6) Get the pointer to the buffer

        // 1) Set the pointer to the buffer in a definitely-free position
        GetGlobal(memoryBoundary) <:> Const(bufferSize) <:> Add <:>
        GetGlobal(memoryBoundary) <:> Store <:>
        // 2) Set the length
        GetGlobal(memoryBoundary) <:> Const(bufferSize+4) <:> Add <:>
        Const(bufferSize - 1) <:> Store <:>
        // 3) Call fd_read
        // 3.1) File descriptor (0 for stdin)
        Const(0) <:>
        // 3.2) Pointer to the list
        GetGlobal(memoryBoundary) <:> Const(bufferSize) <:> Add <:>
        // 3.3) Length of the list
        Const(1) <:>
        // 3.4) Pointer to the output size
        GetGlobal(memoryBoundary) <:> Const(bufferSize+8) <:> Add <:>
        // 3.5) Call fd_read
        Call("fd_read") <:>
        // 4) Check the return value (if an error occurred, we trap)
        If_void <:>
          Unreachable <:>
        Else <:>
        End <:>
        // 5) Get the number of bytes read
        GetGlobal(memoryBoundary) <:> Const(bufferSize+8) <:> Add <:> Load <:>
        SetLocal(bytesRead) <:>
        // 6) Load the pointer to the buffer
        GetGlobal(memoryBoundary) <:>
        // Increment the memory boundary by the number of bytes read
        GetLocal(bytesRead) <:> GetGlobal(memoryBoundary) <:> Add <:> SetGlobal(memoryBoundary) <:>
        // Add padding and null termination. We can do this because we read at most bufferSize-1 bytes.
        Loop(label) <:>
          // Add padding/null termination
          GetGlobal(memoryBoundary) <:> Const(0) <:> Store8 <:>
          GetGlobal(memoryBoundary) <:> Const(1) <:> Add <:> SetGlobal(memoryBoundary) <:>
          // Check if the allocator is aligned.
          GetGlobal(memoryBoundary) <:> Const(4) <:> Rem <:>
          If_void <:>
            // If not, jump back to the loop.
            Br(label) <:>
          Else <:>
          End <:>
        End
      }

      val lenLoop = getFreshLabel()
      val cloneLoop = getFreshLabel()
      val paddingLoop = getFreshLabel()

      GetGlobal(inputBuffer) <:>
      If_i32 <:>
        // The input buffer has already been read at least once. Check if we're at the end!
        GetGlobal(inputBuffer) <:>
        Load8_u <:>
        Const(0) <:>
        Eq <:>
      Else <:>
        // Special case: the read buffer has never been read
        // We thus always need to read.
        Const(1) <:>
      End <:>
      // If we need to, read from the host.
      If_void <:>
        readFromHost <:>
        SetGlobal(inputBuffer) <:>
      Else <:>
      End <:>
      // The start of the string:
      GetGlobal(inputBuffer) <:>
      // Start reading a single string.
      GetGlobal(inputBuffer) <:>
      SetLocal(ptr) <:>
      Const(0) <:>
      SetLocal(bytesRead) <:>
      Loop(lenLoop) <:>
        GetLocal(ptr) <:> Load8_u <:>
        If_void <:>
          GetLocal(ptr) <:> Load8_u <:>
          Const('\n'.toInt) <:> Eq <:>
          If_void <:>
            // Set the input buffer to the next element of the read
            GetLocal(ptr) <:> Const(1) <:> Add <:> SetGlobal(inputBuffer) <:>
          Else <:>
            incr(bytesRead) <:>
            incr(ptr) <:>
            Br(lenLoop) <:>
          End <:>
        Else <:>
          // Input buffer is empty! Next time we'll have to reread.
          GetLocal(ptr) <:> SetGlobal(inputBuffer) <:>
        End <:>
      End <:>
      SetLocal(ptr) <:>
      GetGlobal(memoryBoundary) <:> SetLocal(ptrD) <:>
      // bytesRead = number of non-zero bytes in the string. Time to clone!
      Loop(cloneLoop) <:>
        GetLocal(bytesRead) <:>
        If_void <:>
          GetLocal(ptrD) <:>
          GetLocal(ptr) <:> Load8_u <:>
          Store8 <:>
          incr(ptr) <:>
          incr(ptrD) <:>
          GetLocal(bytesRead) <:> Const(1) <:> Sub <:> SetLocal(bytesRead) <:>
          Br(cloneLoop) <:>
        Else <:>
        End <:>
      End <:>
      Loop(paddingLoop) <:>
        // Write 0
        GetLocal(ptrD) <:> Const(0) <:> Store8 <:>
        // Check if multiple of 4
        GetLocal(ptrD) <:> Const(4) <:> Rem <:>
        Const(3) <:> Eq <:>
        // If not
        If_void <:>
        Else <:>
          // Increment pointer and go back
          incr(ptrD) <:>
          Br(paddingLoop) <:>
        End <:>
      End <:>
      GetGlobal(memoryBoundary) <:>
      GetLocal(ptrD) <:> Const(1) <:> Add <:> SetGlobal(memoryBoundary)
    }
  }

  val readIntImpl: Function = {
    Function("Std_readInt", 0, false) { lh =>
      val isNegative = lh.getFreshLocal()
      val ptr = lh.getFreshLocal()
      val tmp = lh.getFreshLocal()

      // Initialized to 0, by convention.
      val value = lh.getFreshLocal()

      val loop = getFreshLabel()

      // Reads a digit from ptr and leaves it on the stack
      // Traps if there is no digit.
      def readDigit: Code = {
        GetLocal(ptr) <:> Load8_u <:>
        Const('0'.toInt) <:> Sub <:>
        SetLocal(tmp) <:> GetLocal(tmp) <:>
        Const(10) <:> Lt_u <:>
        If_i32 <:>
          GetLocal(tmp) <:>
        Else <:>
          Unreachable <:>
        End
      }

      // First, read a string
      GetGlobal(memoryBoundary)
      Call("Std_readString") <:>
      SetLocal(ptr) <:>
      // Then, convert it to an integer
      // Check if there's a minus sign
      GetLocal(ptr) <:> Load8_u <:>
      Const('-'.toInt) <:> Eq <:>
      If_void <:>
        // Is negative
        incr(ptr) <:>
        Const(1) <:>
        SetLocal(isNegative) <:>
      Else <:>
      End <:>
      // Then, read all remaining digits
      Loop(loop) <:>
        GetLocal(ptr) <:> Load8_u <:>
        Const(0) <:> Eq <:>
        If_void <:>
        Else <:> // Not 0! Read the digit.
          GetLocal(value) <:>
          Const(10) <:>
          Mul <:>
          readDigit <:>
          Add <:>
          SetLocal(value) <:>
          incr(ptr) <:>
          Br(loop) <:>
        End <:>
      End <:>
      // Finally, negate if necessary
      GetLocal(isNegative) <:>
      If_i32 <:>
        Const(0) <:>
        GetLocal(value) <:>
        Sub <:>
      Else <:>
        GetLocal(value) <:>
      End
    }
  }

  val printStringImpl: Function = {
    Function("Std_printString", 1, false) { lh =>
      // To print a string and a newline, we need to:
      // 1) Turn our null-terminated string into a wide pointer
      // 2) Call fd_write
      // 3) Trap if the return value is not 0 (I/O error)
      // 4) Get the number of bytes written
      // 5) Create a wide pointer to a string of a single newline.
      // 6) Call fd_write
      // 7) Trap if the return value is not 0 (I/O error)
      // 8) Return the number of bytes written

      // 1) Turn our null-terminated string into a wide pointer
      GetGlobal(memoryBoundary) <:>
      GetLocal(0) <:> Store <:>
      GetGlobal(memoryBoundary) <:> Const(4) <:> Add <:>
      // Get the string length
      GetLocal(0) <:> Call("String_len") <:> Store <:>
      // 2) Call fd_write
      // 2.1) File descriptor (1 for stdout)
      Const(1) <:>
      // 2.2) Pointer to the list
      GetGlobal(memoryBoundary) <:>
      // 2.3) Length of the list
      Const(1) <:>
      // 2.4) Pointer to the output size
      GetGlobal(memoryBoundary) <:> Const(8) <:> Add <:>
      Call("fd_write") <:>
      // 3) Trap if the return value is not 0 (I/O error)
      If_void <:>
        Unreachable <:>
      Else <:>
      End <:>
      // 4) Load the number of bytes written
      GetGlobal(memoryBoundary) <:> Const(8) <:> Add <:> Load <:>
      // 5) Create a wide pointer to a string of a single newline
      // 5.1) Write the newline
      GetGlobal(memoryBoundary) <:> Const(12) <:> Add <:>
      Const('\n'.toInt) <:> Store <:>
      // 5.2) Write the pointer
      GetGlobal(memoryBoundary) <:>
      GetGlobal(memoryBoundary) <:> Const(12) <:> Add <:> Store <:>
      // 5.3) Write the length
      GetGlobal(memoryBoundary) <:> Const(4) <:> Add <:>
      Const(1) <:> Store <:>
      // 6) Call fd_write
      // 6.1) File descriptor (1 for stdout)
      Const(1) <:>
      // 6.2) Pointer to the list
      GetGlobal(memoryBoundary) <:>
      // 6.3) Length of the list
      Const(1) <:>
      // 6.4) Pointer to the output size
      GetGlobal(memoryBoundary) <:> Const(8) <:> Add <:>
      Call("fd_write") <:>
      // 7) Trap if the return value is not 0 (I/O error)
      If_void <:>
        Unreachable <:>
      Else <:>
      End
      // 8) Return the number of bytes written
      // (implicit via the stack)
    }
  }

  val wasmFunctions = List(stringLenImpl, concatImpl, digitToStringImpl, readStringImpl, readIntImpl, printStringImpl)
}
