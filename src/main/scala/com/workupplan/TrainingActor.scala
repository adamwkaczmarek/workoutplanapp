package com.workupplan

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.workupplan.DrawDistanceActor.GetDistanceResponse
import com.workupplan.DrawExerciseActor.{ GetExerciseResponse}
import com.workupplan.TrainingActor.CreateWorkoutplanRequest
import com.workupplan.dynamodb.DynamoDB


object TrainingActor {

  def props(drawDistanceActor: ActorRef, drawExerciseActor: ActorRef, dynamoDBClient: DynamoDB) = Props(new TrainingActor(drawDistanceActor, drawExerciseActor, dynamoDBClient))

  case class CreateWorkoutplanRequest()

}

class TrainingActor(drawDistanceActor: ActorRef, drawExerciseActor: ActorRef, dynamoDBClient: DynamoDB) extends Actor with ActorLogging {

  override def receive: Receive = {
    case CreateWorkoutplanRequest => {
      log.info(s"TrainingActor#receive#CreateWorkoutplanRequest")
      val trainingSteps = dynamoDBClient.getAllItems("training_steps")
      trainingSteps.foreach(
        _.get("name").foreach(
          x => drawDistanceActor ! DrawDistanceActor.GetDistancesInfoRequest(x.getS))
      )
    }
    case GetDistanceResponse(stepName,maybeItem) => {
      log.info(s"TrainingActor#receive#DrawDistanceResponse(${stepName},${maybeItem}")
      val pattern = raw"ex[0-9]_distance".r

      maybeItem.foreach(
        item => item.foreach(x => x._1 match {
          case pattern(_*) => drawExerciseActor !  DrawExerciseActor.GetExerciseRequest(stepName,x._2.getN.toInt)
          case _=> Actor.emptyBehavior
        }))
    }
    case  GetExerciseResponse(stepName,distance,maybeItem)=>{
      log.info(s"TrainingActor#receive#DrawExcercisecResponse(${stepName},${distance},${maybeItem}")
      Actor.emptyBehavior

    }
  }



}
