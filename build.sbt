name := "todo"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.typesafe.akka" %% "akka-http"   % "10.1.11"
libraryDependencies += "de.heikoseeberger" %% "akka-http-circe" % "1.30.0"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "com.pauldijou" %% "jwt-circe" % "4.2.0"

val circeVersion = "0.12.3"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
