package com.workupplan.mongo



import com.mongodb.client.MongoDatabase

import scala.collection.convert.Wrappers._


class DB private(underlying: MongoDatabase){

  def collectionNames =  for(
    name <- JIterableWrapper(underlying.listCollectionNames)
  )yield name

}

object DB {
  def apply(underlying: MongoDatabase): DB = new DB(underlying)
}
