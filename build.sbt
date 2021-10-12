import Dependencies._

name := "page-view-count-analytics"
version := "0.1"

ThisBuild / scalaVersion := "2.11.12"

lazy val commonSettings = Seq(
  libraryDependencies := dependencies
)

lazy val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
    case PathList("mozilla", _@_*) => MergeStrategy.discard
    case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.discard
    case x => {
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
    }
  }
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    assemblySettings
  )
