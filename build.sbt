ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.13.8"

val AkkaVersion = "2.6.19"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.1" // for html scrapping
libraryDependencies += "com.github.daddykotex" %% "courier" % "3.0.1" // for sending emails
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.11"

lazy val root = (project in file("."))
  .settings(
    name := "html-item-restocked-notifier"
  )
