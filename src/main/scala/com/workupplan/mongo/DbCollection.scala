package com.workupplan.mongo
import com.mongodb.client.{FindIterable, MongoCollection}
import org.bson.Document
import org.bson.conversions.Bson



class DbCollection(override val underlying: MongoCollection[Document]) extends ReadOnly {

}


trait ReadOnly{
  val underlying: MongoCollection[Document]

  def findAll : FindIterable[Document] = underlying find()

  def find(filer: Bson): FindIterable[Document] = underlying find(filer)

  def getCount = underlying count

}


trait Administrable extends ReadOnly{
  def drop:Unit=underlying drop
  def dropIndexes=underlying dropIndexes
}

trait Updatable extends  ReadOnly{

   def +=(doc:Document)=underlying insertOne(doc)
   def -=(doc:Document)=underlying deleteOne(doc)
}