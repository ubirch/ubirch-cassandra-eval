import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.ubirch",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Quill",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      quill,
      logback
    )
  )

