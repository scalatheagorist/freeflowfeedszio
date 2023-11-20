ThisBuild / scalaVersion := "2.13.10"

Compile / mainClass := Some("org.scalatheagorist.freeflowfeedszio.AppServer")

ThisBuild / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.first
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
  case "application.conf" => MergeStrategy.concat
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

val options = Seq(
  "-encoding", "utf8", // Specify character encoding used by source files.
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
  "-unchecked"
)

lazy val root = (project in file("."))
  .settings(
    organization := "org.scalatheagorist",
    name := "freeflowfeedszio",
    version := "1.0-SNAPSHOT",
    scalacOptions ++= options,
    Test / scalaSource := baseDirectory.value / "test" / "scala",
    Compile / scalacOptions ++= (Seq("-nowarn") ++ options),
    Test / resourceDirectory := baseDirectory.value / "test" / "resources",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.4.7" % Runtime,

      "net.codingwell" %% "scala-guice" % "5.1.1",

      "org.typelevel" %% "cats-core" % "2.10.0",

      "dev.zio" %% "zio" % "2.0.18",
      "dev.zio" %% "zio-streams" % "2.0.18",
      "dev.zio" %% "zio-interop-cats" % "23.1.0.0",
      "dev.zio" %% "zio-config" % "3.0.7",
      "dev.zio" %% "zio-config-magnolia" % "3.0.7",
      "dev.zio" %% "zio-config-typesafe" % "3.0.7",
      "dev.zio" %% "zio-nio" % "2.0.2",
      "dev.zio" %% "zio-logging" % "2.1.14",
      "dev.zio" %% "zio-logging-slf4j" % "2.1.14",
      "dev.zio" %% "zio-http" % "3.0.0-RC2",

      "net.ruippeixotog" %% "scala-scraper" % "3.1.0",

      "org.mockito" %% "mockito-scala-scalatest" % "1.17.12" % "test",
      "org.mockito" % "mockito-inline" % "3.12.4" % "test",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
    )
  )

Test / fork := true // @see https://github.com/sbt/sbt/issues/3022
Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oSD")
