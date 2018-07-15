package com.workupplan.mongo

import com.mongodb.{DB => MongoDB}

import scala.collection.convert.Wrappers.JSetWrapper


class DB private(underlying:MongoDB){

  def collectionNames =  for(
    name <- JSetWrapper(underlying.getCollectionNames)
  )yield name

}

object DB {
  def apply(underlying: MongoDB): DB = new DB(underlying)
}
