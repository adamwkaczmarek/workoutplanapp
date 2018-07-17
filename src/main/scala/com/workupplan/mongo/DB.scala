package com.workupplan.mongo



import com.mongodb.client.{MongoCollection, MongoDatabase}
import org.bson.Document

import scala.collection.convert.Wrappers._


class DB private(underlying: MongoDatabase){

  def collectionNames =  for(
    name <- JIterableWrapper(underlying.listCollectionNames)
  )yield name

  private def collection(name:String):MongoCollection[Document]=underlying getCollection(name)

  def readOnlyCollection(name:String)= new DbCollection(collection(name))

  def administrableCollection(name:String)= new DbCollection(collection(name)) with Administrable

  def updatableCollection(name:String)= new DbCollection(collection(name)) with Updatable

}

object DB {
  def apply(underlying: MongoDatabase): DB = new DB(underlying)
}
