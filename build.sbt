import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.remisiki"
ThisBuild / organizationName := "remisiki"

lazy val root = (project in file("."))
  .settings(
    name := "um-course",
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

libraryDependencies += "org.jsoup" % "jsoup" % "1.15.1"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.36.0.3"



assembly / assemblyMergeStrategy := {
  case r if r.startsWith("reference.conf")          => MergeStrategy.concat
  case manifest if manifest.contains("MANIFEST.MF") =>
    // We don't need manifest files since sbt-assembly will create
    // one with the given settings
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    // Keep the content for all reference-overrides.conf files
    MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
  // case x =>
  //  // For all the other files, use the default sbt-assembly merge strategy
  //  val oldStrategy = (assemblyMergeStrategy in assembly).value
  //  oldStrategy(x)
}