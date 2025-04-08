package amyc

import ast._
import utils._
import parsing._
import analyzer._
import codegen._
import interpreter.Interpreter
import java.io.File

object Main extends MainHelpers {
  private def parseArgs(args: Array[String]): Context = {
    var ctx = Context(new Reporter, Nil)
    args foreach {
      case "--printTokens" => ctx = ctx.copy(printTokens = true)
      case "--printTrees"  => ctx = ctx.copy(printTrees = true)
      case "--printNames"  => ctx = ctx.copy(printNames = true)
      case "--interpret"   => ctx = ctx.copy(interpret = true)
      case "--type-check"  => ctx = ctx.copy(typeCheck = true)
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
          |Default behavior is to compile the program to WebAssembly and print the following files:
          |(1) the resulting code in WebAssembly text format (.wat),
          |(2) the resulting code in WebAssembly binary format (.wasm),
          |
          |Options:
          |  --printTokens    Print lexer tokens (with positions) after lexing and exit
          |  --printTrees     Print trees after parsing and exit
          |  --printNames     Print trees with unique namas after name analyzer and exit
          |  --interpret      Interpret the program instead of compiling
          |  --type-check     Type-check the program and print trees
          |  --help           Print this message
        """.stripMargin
      }
      println(helpMsg)
      sys.exit(0)
    }
    val pipeline = {
      AmyLexer.andThen(
        if (ctx.printTokens) DisplayTokens
        else Parser.andThen(
          if (ctx.printTrees) treePrinterN("Trees after parsing")
          else NameAnalyzer.andThen(
            if (ctx.printNames) treePrinterS("Trees after name analysis")
            else TypeChecker.andThen(
              if (ctx.typeCheck) then treePrinterS("Trees after type checking")
              else (
                if (ctx.interpret) then Interpreter
                else CodeGen.andThen(CodePrinter))))))}

    val files = ctx.files.map(new File(_))

    try {
      if (files.isEmpty) {
        ctx.reporter.fatal("No input files")
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

  def treePrinterS(title: String): Pipeline[(SP, SymbolTable), Unit] = {
    new Pipeline[(SP, SymbolTable), Unit] {
      def run(ctx: Context)(v: (SP, SymbolTable)) = {
        println(title)
        println(SymbolicPrinter(v._1)(true))
      }
    }
  }

  def treePrinterN(title: String): Pipeline[NP, Unit] = {
    new Pipeline[NP, Unit] {
      def run(ctx: Context)(v: NP) = {
        println(title)
        println(NominalPrinter(v))
      }
    }
  }
}
