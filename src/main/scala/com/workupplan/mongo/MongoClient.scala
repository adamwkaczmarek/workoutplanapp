package com.workupplan.mongo

import com.mongodb.Mongo

class MongoClient(val host:String,val port:Int) {
  require(host!=null,"Host is required")
  private val underlying= new Mongo(host,port)
  def this()=this("127.0.0.1",27017)

  def version = underlying.getVersion
  def dropDB(name:String)=underlying.dropDatabase(name)
  def createDB(name:String)=DB(underlying.getDB(name))
  def db(name:String) = DB(underlying.getDB(name))

}
