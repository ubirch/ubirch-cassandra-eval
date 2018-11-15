import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.ubirch",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Quill",
    libraryDependencies += scalaTest % Test
  )


libraryDependencies ++= Seq(
  "io.getquill" %% "quill-cassandra" % "2.6.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
