package amyc
package codegen

import amyc.utils.Context
import amyc.utils.Env
import amyc.utils.Pipeline

import java.io.*
import scala.sys.process.*

import wasm.Module

// Prints all 4 different files from a wasm Module
object CodePrinter extends Pipeline[Module, Unit]{
  def run(ctx: Context)(m: Module) = {
    val outDirName = "wasmout"

    def pathWithExt(ext: String) = s"$outDirName/${nameWithExt(ext)}"
    def nameWithExt(ext: String) = s"${m.name}.$ext"

    val (local, inPath) = {
      import Env._
      os match {
        case Linux   => ("./bin/linux/wat2wasm",       "wat2wasm")
        case Windows => ("./bin/windows/wat2wasm.exe", "wat2wasm.exe")
        case Mac     => ("./bin/macos/wat2wasm",       "wat2wasm")
      }
    }

    val w2wOptions = s"${pathWithExt("wat")} -o ${pathWithExt("wasm")}"

    val outDir = new File(outDirName)
    if (!outDir.exists()) {
      outDir.mkdir()
    }

    m.writeWasmText(pathWithExt("wat"))

    try {
      try {
        s"$local $w2wOptions".!!
      } catch {
        case _: IOException =>
          s"$inPath $w2wOptions".!!
      }
    } catch {
      case _: IOException =>
        ctx.reporter.fatal(
          "wat2wasm utility was not found under ./bin/{platform} or in system path, " +
          "or did not have permission to execute. Make sure it is either in the system path, or in <root of the project>/bin/{platform}"
        )
      case _: RuntimeException =>
        ctx.reporter.fatal(s"wat2wasm failed to translate WebAssembly text file ${pathWithExt("wat")} to binary")
    }
  }
}
