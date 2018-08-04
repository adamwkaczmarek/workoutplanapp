package com.workupplan.mongo

import com.mongodb.client.{FindIterable, MongoCollection, MongoCursor}
import org.bson.Document
import org.bson.conversions.Bson


class DbCollection(override val underlying: MongoCollection[Document]) extends ReadOnly {

}


trait ReadOnly {
  val underlying: MongoCollection[Document]

  def findAll = underlying find()

  def find(query: Query) = {

    def applyOption(findIterable: FindIterable[Document],queryOption: QueryOption):FindIterable[Document]={
      queryOption match {
        case SortOption(sort,nextQuery)=>applyOption(findIterable.sort(sort),nextQuery)
        case SkipOption(skip,nextQuery)=>applyOption(findIterable.skip(skip),nextQuery)
        case  LimitOption(linit,nexyQuery)=>applyOption(findIterable.limit(linit),nexyQuery)
        case NoOption => findIterable
      }
    }
    applyOption(underlying find (query.filter),query.queryOption)

  }

  def getCount = underlying count

}


trait Administrable extends ReadOnly {
  def drop: Unit = underlying drop

  def dropIndexes = underlying dropIndexes
}

trait Updatable extends ReadOnly {

  def +=(doc: Document) = underlying insertOne (doc)

  def -=(doc: Document) = underlying deleteOne (doc)
}

case class Query(filter: Bson, queryOption: QueryOption=NoOption) {
  def sort(sort: Bson) = Query(filter, SortOption(sort, queryOption))

  def skip(skip: Int) = Query(filter, SkipOption(skip, queryOption))

  def limit(limit: Int) = Query(filter, LimitOption(limit, queryOption))

}


sealed trait QueryOption

case object NoOption extends QueryOption

case class SortOption(sort: Bson, nextQuery: QueryOption) extends QueryOption

case class SkipOption(skip: Int, nextQuery: QueryOption) extends QueryOption

case class LimitOption(limit: Int, nexyQuery: QueryOption) extends QueryOption

