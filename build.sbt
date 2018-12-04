import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "advent-of-code-2018",
    scalacOptions += "-Ypartial-unification",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "co.fs2" %% "fs2-core" % "1.0.1",
    libraryDependencies += "co.fs2" %% "fs2-io" % "1.0.0"
  )
