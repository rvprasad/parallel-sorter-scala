/*
 * Copyright (c) 2018, Venkatesh-Prasad Ranganath
 *
 * BSD 3-clause License
 *
 * Author: Venkatesh-Prasad Ranganath (rvprasad)
 *
 */

scalacOptions += "-deprecation"

ThisBuild / organization := "com.rvprasad"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "1.0"

name := "single-node-multi-core-sorter"
libraryDependencies ++= {
  Seq(
    "commons-cli" % "commons-cli" % "1.4",
    "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
  )
}

fork in run := true

test in assembly := {}
assemblyJarName in assembly := s"snmc-assembly-${version.value}.jar"
mainClass in assembly := Some("sorter.parallel.Main")

//Test / parallelExecution := true
//testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck,
//    "-workers", "1", "-verbosity", "1")
