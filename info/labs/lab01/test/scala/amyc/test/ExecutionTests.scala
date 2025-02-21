package amyc.test

import org.junit.Test

abstract class ExecutionTests extends TestSuite {

  val baseDir = "execution"

  val outputExt = "txt"

  @Test def testEmptyObject = shouldOutput("EmptyObject")

  @Test def testStringInputToOutput = shouldOutput(List("Std", "StringInputToOutput"), "StringInputToOutput", "Hello World\nHello World again")

  @Test def testIntInputToOutput = shouldOutput(List("Std", "IntInputToOutput"), "IntInputToOutput", "42")

  @Test def testMixStdAndNonStd = shouldOutput(List("Std", "MixStdAndNonStd"), "MixStdAndNonStd", "42")

  @Test def testBasicArithmetic = shouldOutput(List("Std", "BasicArithmetic"), "BasicArithmetic", "")

  @Test def testDivisionByZero = shouldFail("Division")

  @Test def testBasicPatternMatching = shouldOutput(List("Std", "BasicPatternMatching"), "BasicPatternMatching", "")

  @Test def testBasicBranching = shouldOutput(List("Std", "BasicBranching"), "BasicBranching", "")

  @Test def testBasicConditions = shouldOutput(List("Std", "BasicConditions"), "BasicConditions", "")

  @Test def testBasicError = shouldFail("BasicError")

  @Test def testEquality = shouldOutput(List("Std", "Equality"), "Equality")

  @Test def testFactorial = shouldOutput(List("Std", "Factorial"), "Factorial", "")

  @Test def testArithmetic = shouldOutput(List("Std", "Arithmetic"), "Arithmetic", "")

  @Test def testLists = shouldOutput(List("Std", "Option", "List", "TestLists"), "TestLists", "")

  @Test def testMatchError1 = shouldFail("MatchError1")

  @Test def testMatchError2 = shouldFail("MatchError2")

  @Test def testMatchError3 = shouldFail("MatchError3")

  @Test def testMatchError4 = shouldFail("MatchError4")

  @Test def testMatch1 = shouldPass("Match1")

  @Test def testMatch2 = shouldPass("Match2")

  @Test def testMatch3 = shouldPass("Match3")

  @Test def testMatch4 = shouldOutput(List("Std", "Match4"), "Match4")

  @Test def testShortCircuit1 = shouldPass("ShortCircuit")

  @Test def testShortCircuit2 = shouldFail("ShortCircuit")

  @Test def testLocals1 = shouldPass("Locals")

  @Test def testLocals2 = shouldFail("Locals")

  @Test def testFunCallEnv = shouldOutput(List("FunCallEnv", "Std"), "FunCallEnv")
}
