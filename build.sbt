name := "workoutPlanApp"

version := "0.1"

scalaVersion := "2.12.6"



libraryDependencies ++= Seq(
  "org.mongodb" % "mongo-java-driver" % "3.6.4",
  "com.typesafe.akka" %% "akka-actor" % "2.5.3" ,
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.385")


// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked")