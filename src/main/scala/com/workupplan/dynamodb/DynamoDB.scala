package com.workupplan.dynamodb

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, GetItemRequest, ScanRequest}
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClient}

import scala.collection.JavaConverters._

object DynamoDB {


  def apply(region: Regions): DynamoDB = {
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

case class DynamoDB(client: AmazonDynamoDB) {

  def getItem(tabelName: String, keyToGet: Map[String, String]):Map[String, AttributeValue] = {
    val getItemRequest = new GetItemRequest()
      .withTableName(tabelName)
      .withKey(keyToGet.map(x => (x._1, new AttributeValue(x._2))).asJava)
    client.getItem(getItemRequest).getItem.asScala.toMap
  }

  def getAllItems(tableName:String):List[Map[String, AttributeValue]]={
    val scanRequest= new ScanRequest().withTableName(tableName)
    client.scan(scanRequest).getItems.asScala.map(_.asScala.toMap).toList
  }

}
