package amyc.utils

object Env {
  trait OS
  object Linux extends OS
  object Windows extends OS
  object Mac extends OS

  lazy val os = {
    // If all fails returns Linux
    val optOsName = Option(System.getProperty("os.name"))
    optOsName.map(_.toLowerCase()).map { osName =>
      if (osName.contains("linux")) then Linux
      else if (osName.contains("win")) then Windows
      else if (osName.contains("mac")) then Mac
      else Linux
    } getOrElse Linux
  }
}
