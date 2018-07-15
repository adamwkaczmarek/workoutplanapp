name := "workoutPlanApp"

version := "0.1"

scalaVersion := "2.12.6"



libraryDependencies += "org.mongodb" % "mongo-java-driver" % "2.10.1"


// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked")