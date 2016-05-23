name := "picvote"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.5"

libraryDependencies += "io.spray" %% "spray-client" % "1.3.3"

libraryDependencies += "io.spray" %% "spray-routing" % "1.3.3"

libraryDependencies += "io.spray" %% "spray-httpx" % "1.3.3"

libraryDependencies += "io.spray" %% "spray-json" % "1.3.2"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.12"

libraryDependencies += "ch.qos.logback" % "logback-classic"  % "1.1.3"

mainClass in (Compile, run) := Some("picvote.Main")
