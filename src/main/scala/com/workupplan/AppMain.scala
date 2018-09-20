package com.workupplan

import akka.actor.{ActorRef, ActorSystem}
import com.amazonaws.regions.Regions
import com.workupplan.dynamodb.DynamoDB

import scala.concurrent.ExecutionContext


object AppMain extends App {

  private val actorSystem = ActorSystem("workoutplan-system")
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  val dynamoDb= DynamoDB(Regions.US_EAST_1)

  private val trainingActor: ActorRef = actorSystem.actorOf(TrainingActor.props(dynamoDb), "training")

  trainingActor ! (TrainingActor CreateWorkoutplanRequest)

}
