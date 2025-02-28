package amyc

import utils._

import java.io.File

import amyc.parsing._

object Main {
  private def parseArgs(args: Array[String]): Context = {
    var ctx = Context(new Reporter, Nil)
    args foreach {
      case "--interpret"   => ctx = ctx.copy(interpret = true)
      case "--help"        => ctx = ctx.copy(help = true)
      case "--type-check"  => ctx = ctx.copy(typeCheck = true)
      case "--printTokens" => ctx = ctx.copy(printTokens = true)
      case file             => ctx = ctx.copy(files = ctx.files :+ file)
    }
    ctx
  }

  def main(args: Array[String]): Unit = {
    val ctx = parseArgs(args)
    val pipeline = AmyLexer.andThen(DisplayTokens)

    if ctx.help then
      println("Usage: amyc [ --interpret | --type-check | --printTokens ] file1.amy file2.amy ...")
      sys.exit(0)
    val files = ctx.files.map(new File(_))

    try {
      if (files.isEmpty) {
        ctx.reporter.fatal("No input files")
      }
      files.find(!_.exists()).foreach { f =>
        ctx.reporter.fatal(s"File not found: ${f.getName}")
      }
      if ctx.interpret || ctx.typeCheck then
        ctx.reporter.fatal("Unsupported actions for now")
      else if ctx.printTokens then
        pipeline.run(ctx)(files)
        ctx.reporter.terminateIfErrors()
      else
        ctx.reporter.fatal("No action specified")
      ctx.reporter.terminateIfErrors()
    } catch {
      case AmycFatalError(_) =>
        sys.exit(1)
    }
  }
}