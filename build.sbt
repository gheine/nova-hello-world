
name := "hello_world"

version := "git describe --tags --dirty --always".!!.stripPrefix("v").trim

scalaVersion := "2.12.2"

val http4sVersion = "0.15.13a"

libraryDependencies ++= Seq(
  "org.http4s"           %% "http4s-core"               % http4sVersion,
  "org.http4s"           %% "http4s-dsl"                % http4sVersion,
  "org.http4s"           %% "http4s-blaze-server"       % http4sVersion
)

dependencyOverrides ++= Set("org.slf4j" % "slf4j-api" % "1.7.25" % "it")

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked", "-language:_")

cancelable := true

fork := true

logBuffered := false

enablePlugins(JavaServerAppPackaging)

maintainer in Docker := "HBC Mobile <hbc-mobile@gilt.com>"

dockerBaseImage := "anapsix/alpine-java:8_server-jre_unlimited"

defaultLinuxInstallLocation in Docker := "/opt/hello_world"

mappings in Universal ++= {
  val base = baseDirectory.value
  val confDir = base / "conf"

  for {
    (file, relativePath) <-  (confDir.*** --- confDir) x relativeTo(confDir)
  } yield file -> s"/conf/$relativePath"
}

