package amyc.test

import amyc.analyzer.NameAnalyzer
import amyc.analyzer.TypeChecker
import amyc.codegen.*
import amyc.parsing.*
import amyc.utils.*
import amyc.wasm.Module

import java.io.ByteArrayInputStream
import scala.collection.JavaConverters.*
import scala.sys.process.*

class CodegenTests extends ExecutionTests {

  object CodePrinterExecutor extends Pipeline[Module, Unit] {
    def run(ctx: Context)(m: Module) = {
      CodePrinter.run(ctx)(m)
      val fileName = s"${m.name}.wasm"

      // Consume all standard input!
      val input = Console.in.lines.iterator().asScala.toList.mkString("\n")
      val inputS = new ByteArrayInputStream(input.getBytes("UTF-8"))

      val binary = {
        import Env._
        os match {
          case Linux => "./bin/linux/wasmtime"
          case Windows => "./bin/windows/wasmtime.exe"
          case Mac => "./bin/macos/wasmtime"
        }
      }

      val exitCode = s"$binary wasmout/$fileName" #< inputS ! ProcessLogger(Console.out.println, Console.err.println)
      if (exitCode != 0) {
        throw AmycFatalError("Nonzero code returned from wasmtime.")
      }
    }
  }

  val pipeline =
    AmyLexer
    .andThen(Parser)
    .andThen(NameAnalyzer)
    .andThen(TypeChecker)
    .andThen(CodeGen)
    .andThen(CodePrinterExecutor)
}
