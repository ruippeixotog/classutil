// ---------------------------------------------------------------------------
// Basic settings

name := "classutil"

organization := "net.ruippeixotog"

version := "1.0.5"

licenses := Seq(
  "BSD New" -> url("http://software.clapper.org/classutil/license.html")
)

homepage := Some(url("http://software.clapper.org/classutil/"))

description := "A library for fast runtime class-querying, and more"

scalaVersion := "2.11.4"

crossScalaVersions := Seq("2.10.4", "2.11.4")

// ---------------------------------------------------------------------------
// Additional compiler options and plugins

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

autoCompilerPlugins := true

lsSettings

(LsKeys.tags in LsKeys.lsync) := Seq("classes", "byte code")

(description in LsKeys.lsync) <<= description(d => d)

// ---------------------------------------------------------------------------
// ScalaTest dependendency

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.3" % "test"

libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
  // ScalaTest still uses the (deprecated) scala.actors API.
  deps :+ "org.scala-lang" % "scala-actors" % sv % "test"
}

// ---------------------------------------------------------------------------
// Other dependendencies

libraryDependencies ++= Seq(
    "org.ow2.asm" % "asm" % "5.0.2",
    "org.ow2.asm" % "asm-commons" % "5.0.2",
    "org.ow2.asm" % "asm-util" % "5.0.2",
    "org.clapper" %% "grizzled-scala" % "1.2",
    "org.clapper" %% "grizzled-slf4j" % "1.0.2"
)

// ---------------------------------------------------------------------------
// Publishing criteria

publishTo <<= version { v =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <scm>
    <url>https://github.com/ruippeixotog/classutil</url>
    <connection>scm:git:https://github.com/ruippeixotog/classutil.git</connection>
  </scm>
  <developers>
    <developer>
      <id>bmc</id>
      <name>Brian Clapper</name>
      <url>http://www.clapper.org/bmc</url>
    </developer>
  </developers>
)
