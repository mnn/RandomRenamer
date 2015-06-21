package tk.monnef.randomrenamer

object BuildInfoWrapper {
  lazy val buildInfoInstance = Class.forName("tk.monnef.randomrenamer.BuildInfo")
  lazy val version = getAndInvokeMethod[String]("version")
  lazy val title = getAndInvokeMethod[String]("name")
  val name = "randomrenamer"
  lazy val scalaVersion = getAndInvokeMethod[String]("scalaVersion")
  lazy val sbtVersion = getAndInvokeMethod[String]("sbtVersion")
  val createdBy = "Created by monnef"

  def getAndInvokeMethod[T](name: String): T = buildInfoInstance.getMethod(name).invoke(buildInfoInstance).asInstanceOf[T]
}
