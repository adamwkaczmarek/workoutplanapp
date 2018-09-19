package com.workupplan

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.workupplan.DrawExerciseActor.{GetExerciseRequest, GetExerciseResponse}
import com.workupplan.dynamodb.DynamoDB
import com.workupplan.utils.Helpers._

object DrawExerciseActor {
  def props(dynamoDB: DynamoDB)= Props(new DrawExerciseActor(dynamoDB))
  case class GetExerciseRequest(trainingStep:String,distance:Integer)
  case class GetExerciseResponse(stepName:String,distance:Int,item:Option[Map[String,AttributeValue]])

}

class DrawExerciseActor(dynamoDB: DynamoDB) extends Actor with ActorLogging{
  override def receive: Receive = {
    case GetExerciseRequest(stepName,distance:Integer)=>{
      log.info(s"DrawExerciseActor#receive#DrawExerciseRequest(${stepName}, ${distance})")
      val tablename = s"${stepName}_exercises"
      val items = dynamoDB.getAllItems(tablename)
      sender() ! GetExerciseResponse(stepName,distance,items.getRandomElement)
    }
  }
}
