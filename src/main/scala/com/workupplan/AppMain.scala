package com.workupplan

import com.workupplan.mongo.{DB, MongoClient}

object AppMain extends App {

  val mongoClient= new MongoClient()

  private val forTestDB: DB = mongoClient.createDB("twist")

  for( name <-forTestDB.collectionNames)println(name)

}
