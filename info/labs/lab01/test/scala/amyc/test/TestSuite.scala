package amyc.test

import amyc.utils.Pipeline
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

abstract class TestSuite extends CompilerTest {
  val pipeline: Pipeline[List[File], Unit]

  val baseDir: String
  lazy val effectiveBaseDir: String =
    // getClass.getResource(s"/$baseDir").getPath
    s"test/resources/$baseDir"

  val passing = "passing"
  val failing = "failing"
  val outputs = "outputs"

  val tmpDir = Files.createTempDirectory("amyc");

  val outputExt: String

  def getResourcePath(relativePath: String, otherPath: Option[String] = None): String =
    val firstPath = Path.of(effectiveBaseDir, relativePath)

    val (stream, path) = 
      if Files.exists(firstPath) then
        (Files.newInputStream(firstPath), relativePath)
      else
        otherPath match
          case Some(p) =>
            val secondPath = Path.of(effectiveBaseDir, p)
            (Files.newInputStream(secondPath), p)
          case None =>
            assert(false, s"can not read $effectiveBaseDir/$relativePath")
            (null, "")

    val targetPath = tmpDir.resolve(path)
    Files.createDirectories(targetPath.getParent())
    Files.copy(stream, targetPath, StandardCopyOption.REPLACE_EXISTING)
    targetPath.toAbsolutePath().toString()

  def shouldOutput(inputFiles: List[String], outputFile: String, input: String = ""): Unit = {
    compareOutputs(
      pipeline,
      inputFiles map (f => getResourcePath(s"$passing/$f.amy", Some(s"$passing/$f.grading.amy"))),
      getResourcePath(s"$outputs/$outputFile.$outputExt", Some(s"$outputs/$outputFile.grading.$outputExt")),
      input
    )
  }

  def shouldOutput(inputFile: String): Unit = {
    shouldOutput(List(inputFile), inputFile)
  }

  def shouldFail(inputFiles: List[String], input: String = ""): Unit = {
    demandFailure(
      pipeline,
      inputFiles map (f => getResourcePath(s"$failing/$f.amy", Some(s"$failing/$f.grading.amy"))),
      input
    )
  }

  def shouldFail(inputFile: String): Unit = {
    shouldFail(List(inputFile))
  }

  def shouldPass(inputFiles: List[String], input: String = ""): Unit = {
    demandPass(pipeline, inputFiles map (f => getResourcePath(s"$passing/$f.amy", Some(s"$passing/$f.grading.amy"))), input)
  }

  def shouldPass(inputFile: String): Unit = {
    shouldPass(List(inputFile))
  }

}
