package amyc
package test

import parsing._
import analyzer._
import interpreter.Interpreter
import amyc.utils.Frontend

class InterpreterTests extends ExecutionTests {
  val pipeline = Frontend.pipeline.andThen(Interpreter)
}
