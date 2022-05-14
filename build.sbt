ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val AkkaVersion = "2.6.19"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.1" // for html scrapping
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.11"

lazy val root = (project in file("."))
  .settings(
    name := "html-item-restocked-notifier"
  )
