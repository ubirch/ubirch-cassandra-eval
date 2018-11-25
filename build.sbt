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
    quill,
    phantom,
    migrationTools
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
      dependencies.quill,
      dependencies.javax,
      dependencies.guice

    )
  )
  .dependsOn(
    common
  )

lazy val phantom = project
  .settings(
    name := "phantom",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.phantom
    )
  )
  .dependsOn(
    common
  )

lazy val migrationTools = project
  .settings(
    name := "migration-tools",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.cassandraMig,
      dependencies.cassandreDriver
    )
  )
  .dependsOn(
    common
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val logbackV          = "1.2.3"
    val scalatestV        = "3.0.5"
    val quillV            = "2.6.0"
    val alpakkaCassandraV = "1.0-M1"
    val phantomV          = "2.27.0"
    val jodaTimeV         = "2.10"
    val cassandraMigV     = "2.2.0"
    val cassandraDriverV  = "3.6.0"
    val guiceV            = "4.2.2"
    val javaxV            = "1"


    //Basics
    val logback          = "ch.qos.logback" % "logback-classic" % logbackV
    val scalatest        = "org.scalatest" %% "scalatest" % scalatestV
    val jodaTime         = "joda-time" % "joda-time" % jodaTimeV
    //Cassandra Integration Tools
    val quill            = "io.getquill" %% "quill-cassandra" % quillV
    val alpakkaCassandra = "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % alpakkaCassandraV
    val phantom          = "com.outworkers" %% "phantom-dsl" % phantomV
    val cassandreDriver  = "com.datastax.cassandra" % "cassandra-driver-core" % cassandraDriverV
    val cassandraMig     = "org.cognitor.cassandra" % "cassandra-migration" % cassandraMigV
    //DB Migration Tools
    val guice            = "com.google.inject" % "guice" % guiceV
    val javax            = "javax.inject" % "javax.inject" % javaxV
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.scalatest % "test",
  dependencies.jodaTime
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