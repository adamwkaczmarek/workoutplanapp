package com.workupplan

import akka.actor.{ActorRef, ActorSystem}
import com.amazonaws.regions.Regions
import com.workupplan.dynamodb.DynamoDB


object AppMain extends App {

  val dynamoDb= DynamoDB(Regions.US_EAST_1)

  private val actorSystem = ActorSystem("workoutplan-system")
  private val drawDistance: ActorRef = actorSystem.actorOf(DrawDistanceActor.props(dynamoDb), "draw-distance")
  private val drawExcercise: ActorRef = actorSystem.actorOf(DrawExerciseActor.props(dynamoDb), "draw-exercise")
  private val trainingActor: ActorRef = actorSystem.actorOf(TrainingActor.props(drawDistance, drawExcercise,dynamoDb), "training")

  trainingActor ! (TrainingActor CreateWorkoutplanRequest)

}
