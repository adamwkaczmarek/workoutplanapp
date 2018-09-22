package com.workupplan

import akka.actor.{ActorRef, ActorSystem}
import com.amazonaws.regions.Regions
import com.workupplan.dynamodb.DynamoDB

import scala.concurrent.ExecutionContext


object AppMain extends App {

  private val actorSystem = ActorSystem("workoutplan-system")
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  private val dynamoDb= DynamoDB(Regions.US_EAST_1)
  private val trainingService=TrainingService(dynamoDb)
  private val trainingActor: ActorRef = actorSystem.actorOf(TrainingActor.props(trainingService), "training")

  trainingActor ! (TrainingActor CreateWorkoutplanRequest)

}
