package com.workupplan.mongo

import com.mongodb.{MongoClient => MongoDbClient}

class MongoClient(val host:String,val port:Int) {
  require(host!=null,"Host is required")
  private val underlying= new MongoDbClient(host,port)
  def this()=this("127.0.0.1",27017)

  def dropDB(name:String)=underlying.dropDatabase(name)
  def createDB(name:String)=DB(underlying.getDatabase(name))
  def db(name:String) = DB(underlying.getDatabase(name))

}
