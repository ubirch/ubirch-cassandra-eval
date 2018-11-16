import scalariform.formatter.preferences._


name := "ubirch-cassandra-eval"
organization in ThisBuild := "com.ubirch"
scalaVersion in ThisBuild := "2.12.6"


// PROJECTS

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    common,
    alpakka,
    quill
  )

lazy val common = project
  .settings(
    name := "common",
    settings,
    libraryDependencies ++= commonDependencies
  )

lazy val alpakka = project
  .settings(
    name := "alpakka-cassandra",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.alpakkaCassandra
    )
  )
  .dependsOn(
    common
  )

lazy val quill = project
  .settings(
    name := "quill",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.quill
    )
  )
  .dependsOn(
    common
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val logbackV          = "1.2.3"
    val scalaLoggingV     = "3.7.2"
    val slf4jV            = "1.7.25"
    val scalatestV        = "3.0.5"
    val quillV            = "2.6.0"
    val alpakkaCassandraV = "1.0-M1"

    //Basics
    val logback          = "ch.qos.logback"             % "logback-classic"                % logbackV
    val scalaLogging     = "com.typesafe.scala-logging" %% "scala-logging"                 % scalaLoggingV
    val slf4j            = "org.slf4j"                  % "jcl-over-slf4j"                 % slf4jV
    val scalatest        = "org.scalatest"              %% "scalatest"                     % scalatestV

    //Good stuff
    val quill            = "io.getquill"                %% "quill-cassandra"               % quillV
    val alpakkaCassandra = "com.lightbend.akka"         %% "akka-stream-alpakka-cassandra" % alpakkaCassandraV
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.scalatest  % "test"
)

// SETTINGS

lazy val settings = commonSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)



scalariformAutoformat := true
scalariformPreferences := scalariformPreferences.value
  .setPreference(SpacesAroundMultiImports, true)
  .setPreference(SpaceInsideParentheses, false)
  .setPreference(DanglingCloseParenthesis, Preserve)
  .setPreference(PreserveSpaceBeforeArguments, true)
  .setPreference(DoubleIndentConstructorArguments, true)