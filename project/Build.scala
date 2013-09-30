import sbt._
import Keys._

object Panopticon extends Build {
  val scroogeVersion = "3.8.0"

  val sharedSettings = Seq(
    organization := "com.mosesn",
    scalaVersion := "2.10.2",
    version := "0.0.1-SNAPSHOT",
    scalacOptions += "-deprecation"
  )

  val deps = Seq(
    "com.twitter" %% "util-core" % "6.5.0"
  )

  val testDeps = Seq(
    "org.scalatest" %% "scalatest" % "1.9.2" % "test"
  )

  val generalSettings = Project.defaultSettings ++ sharedSettings

  lazy val panopticon = Project(
    id = "panopticon",
    base = file("."),
    settings = generalSettings
  ).aggregate(core)

  def project(name: String): Project = {
    val projectName = "panopticon-%s" format name
    Project(
      id = projectName,
      base = file(projectName),
      settings = generalSettings
    )
  }
  lazy val core = project("core")
    .settings(libraryDependencies ++= deps ++ testDeps)
}
