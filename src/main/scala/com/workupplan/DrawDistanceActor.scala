package com.workupplan

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.workupplan.DrawDistanceActor.{GetDistanceResponse, GetDistancesInfoRequest}
import com.workupplan.dynamodb.DynamoDB
import com.workupplan.utils.Helpers._


object DrawDistanceActor {

  def props(dynamoDBClient: DynamoDB) = Props(new DrawDistanceActor(dynamoDBClient))
  case class GetDistancesInfoRequest(stepName: String)
  case class GetDistanceResponse(stepName:String,item: Option[Map[String, AttributeValue]])

}

class DrawDistanceActor(dynamoDBClient: DynamoDB) extends Actor with ActorLogging {
  override def receive: Receive = {

    case GetDistancesInfoRequest(stepName) => {
      log.info(s"DrawDistanceActor#receive#GetDistancesInfoRequest(${stepName})")
      val tablename = s"${stepName}_distances"
      val items = dynamoDBClient.getAllItems(tablename)
      sender() ! GetDistanceResponse(stepName,items.getRandomElement)


    }

  }
}
