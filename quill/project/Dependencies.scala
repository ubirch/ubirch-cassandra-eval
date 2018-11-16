import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val quill = "io.getquill" %% "quill-cassandra" % "2.6.0"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
}
