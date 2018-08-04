package com.workupplan

import com.mongodb.client.model.{Filters, Sorts}
import com.workupplan.mongo.{DB, MongoClient, Query}
import org.bson.Document

object AppMain extends App {

  val mongoClient= new MongoClient()

  private val db: DB = mongoClient.createDB("twist")
  private val user = db.updatableCollection("users")


  for( name <-db.collectionNames)println(name)

  val document1 = new Document()
  document1.append("name","Adam")
  document1.append("surname","Kaczmarek")
  document1.append("mail","adamw.kacmzarek@gmail.com")

  user += document1

  val document2 = new Document()
  document2.append("name","Natalia")
  document2.append("surname","Kaczmarek")
  document2.append("mail","natak@gmail.com")

  user+=document2



  val documents=user find(Query(Filters.eq("surname","Kaczmarek")).sort(Sorts.orderBy(Sorts.descending("name"))))


  while(documents.iterator.hasNext){
    val document = documents.iterator.next
    println(document.toJson)
    user.-=(document)
  }
  println("END")
}
