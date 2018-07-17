package com.workupplan

import com.mongodb.client.FindIterable
import com.workupplan.mongo.{DB, DbCollection, MongoClient}
import org.bson.Document

object AppMain extends App {

  val mongoClient= new MongoClient()

  private val db: DB = mongoClient.createDB("twist")

  for( name <-db.collectionNames)println(name)

  private val user = db.updatableCollection("users")

  val document = new Document()

  document.append("name","Adam")
  document.append("mail","adamw.kacmzarek@gmail.com")

  user += document

  val documents: FindIterable[Document] = user findAll

//  import com.mongodb.Block
//
//  val printBlock = new Block[Document]() {
//    override def apply(document: Document): Unit = {
//      System.out.println(document.toJson)
//    }
//  }
//
//  documents.forEach(printBlock)


  println("END")
}
