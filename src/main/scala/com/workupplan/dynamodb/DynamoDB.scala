package com.workupplan.dynamodb

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, GetItemRequest, ScanRequest}
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClient}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import com.workupplan.utils.Helpers._

object DynamoDB {


  def apply(region: Regions)(implicit executionContext: ExecutionContext): DynamoDB = {
    val config = new ClientConfiguration()
      .withTcpKeepAlive(true)

    DynamoDB(
      AmazonDynamoDBClient
        .builder()
        .withRegion(region)
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .withClientConfiguration(config)
        .build())
  }
}

case class DynamoDB(client: AmazonDynamoDB)(implicit executionContext: ExecutionContext) {

  def getItem(tabelName: String, keyToGet: Map[String, String]):Future[Map[String, AttributeValue]] = {
   Future {
     val getItemRequest = new GetItemRequest()
       .withTableName(tabelName)
       .withKey(keyToGet.map(x => (x._1, new AttributeValue(x._2))).asJava)
     client.getItem(getItemRequest).getItem.asScala.toMap
   }
  }

  def getAllItems(tableName:String):Future[List[Map[String, AttributeValue]]]={
   Future {
     val scanRequest = new ScanRequest().withTableName(tableName)
     client.scan(scanRequest).getItems.asScala.map(_.asScala.toMap).toList

   }
  }

  def getRandomItem(tableName:String):Future[Map[String, AttributeValue]]=
    getAllItems(tableName).map(_.getRandomElement match {
      case Some(element)=>element
      case None=>Map.empty
    })

}
