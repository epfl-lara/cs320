package amyc

import ast._
import utils._
import parsing._

import java.io.File

object Main extends MainHelpers {
  private def parseArgs(args: Array[String]): Context = {
    var ctx = Context(new Reporter, Nil)
    args foreach {
      case "--printTokens" => ctx = ctx.copy(printTokens = true)
      case "--printTrees"  => ctx = ctx.copy(printTrees = true)
      case "--interpret"   => ctx = ctx.copy(interpret = true)
      case "--help"        => ctx = ctx.copy(help = true)
      case file            => ctx = ctx.copy(files = ctx.files :+ file)
    }
    ctx
  }

  def main(args: Array[String]): Unit = {
    val ctx = parseArgs(args)
    if (ctx.help) {
      val helpMsg = {
        """Welcome to the Amy reference compiler, v.1.5
          |
          |Options:
          |  --printTokens    Print lexer tokens (with positions) after lexing and exit
          |  --printTrees     Print trees after parsing and exit
          |  --interpret      Interpret the program instead of compiling
          |  --help           Print this message
        """.stripMargin
      }
      println(helpMsg)
      sys.exit(0)
    }
    val pipeline = 
      AmyLexer.andThen(
        if (ctx.printTokens) DisplayTokens
        else Parser.andThen(
          treePrinterN("Trees after parsing")))

    val files = ctx.files.map(new File(_))

    try {
      if (files.isEmpty) {
        ctx.reporter.fatal("No input files")
      }
      if (ctx.interpret) {
        ctx.reporter.fatal("Unsupported actions for now")
      }
      files.find(!_.exists()).foreach { f =>
        ctx.reporter.fatal(s"File not found: ${f.getName}")
      }
      pipeline.run(ctx)(files)
      ctx.reporter.terminateIfErrors()
    } catch {
      case AmycFatalError(_) =>
        sys.exit(1)
    }
  }
}

trait MainHelpers {
  import SymbolicTreeModule.{Program => SP}
  import NominalTreeModule.{Program => NP}

  def treePrinterN(title: String): Pipeline[NP, Unit] = {
    new Pipeline[NP, Unit] {
      def run(ctx: Context)(v: NP) = {
        println(title)
        println(NominalPrinter(v))
      }
    }
  }
}