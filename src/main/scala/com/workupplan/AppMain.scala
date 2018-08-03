package com.workupplan

import com.mongodb.client.FindIterable
import com.mongodb.client.model.Filters
import com.workupplan.mongo.{DB, DbCollection, MongoClient}
import org.bson.Document

object AppMain extends App {

  val mongoClient= new MongoClient()

  private val db: DB = mongoClient.createDB("twist")
  private val user = db.updatableCollection("users")


  for( name <-db.collectionNames)println(name)

  val document = new Document()
  document.append("name","Adam")
  document.append("mail","adamw.kacmzarek@gmail.com")

  user += document

  //val documents = user findAll

  val documents=user find(Filters.eq("name","Adam"))


  while(documents.hasNext){
    val document = documents.next()
    println(document.toJson)
    user.-=(document)
  }
  println("END")
}
