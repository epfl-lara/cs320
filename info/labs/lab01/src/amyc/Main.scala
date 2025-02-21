package amyc

import utils._
import interpreter.Interpreter

import java.io.File

object Main {
  private def parseArgs(args: Array[String]): Context = {
    var ctx = Context(new Reporter, Nil)
    args foreach {
      case "--interpret"   => ctx = ctx.copy(interpret = true)
      case "--help"        => ctx = ctx.copy(help = true)
      case "--type-check"  => ctx = ctx.copy(typeCheck = true)
      case file            => ctx = ctx.copy(files = ctx.files :+ file)
    }
    ctx
  }

  def main(args: Array[String]): Unit = {
    val ctx = parseArgs(args)
    val pipelineInterpret = Frontend.pipeline.andThen(Interpreter)

    if ctx.help then
      println("Usage: amyc [ --interpret | --type-check ] file1.amy file2.amy ...")
      sys.exit(0)
    val files = ctx.files.map(new File(_))

    try {
      if (files.isEmpty) {
        ctx.reporter.fatal("No input files")
      }
      files.find(!_.exists()).foreach { f =>
        ctx.reporter.fatal(s"File not found: ${f.getName}")
      }
      if ctx.interpret then
        pipelineInterpret.run(ctx)(files)
      else if ctx.typeCheck then
        Frontend.pipeline.run(ctx)(files)
        println("Type checking successful!")
      else
        ctx.reporter.fatal("No action specified")
      ctx.reporter.terminateIfErrors()
    } catch {
      case AmycFatalError(_) =>
        sys.exit(1)
    }
  }
}