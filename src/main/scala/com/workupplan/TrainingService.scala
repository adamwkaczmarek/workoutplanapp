package com.workupplan


import com.workupplan.dynamodb.DynamoDB

import scala.concurrent.{ExecutionContext, Future}


case class Distance(id:Option[Int],distance:Option[Int],exerciseDistances:Map[String,Int],step:Option[String])
case class Exercise(name: Option[String], displayName: Option[String],distance:Option[Int],step:Option[String])
case class Step(name: Option[String],displayName: Option[String], percent: Option[Int])


case class TrainingService(dynamoDB: DynamoDB)(implicit executionContext: ExecutionContext)  {

  def getRandomDistance(maybeStepName:Option[String]):Future[Distance]={

    maybeStepName match {
      case Some(stepName)=>{ val distanceTableName = s"${stepName}_distances"
        dynamoDB.getRandomItem(distanceTableName).map(
          item=>
            Distance(
              item.get("id").map(_.getN.toInt),
              item.get("distance").map(_.getN.toInt),
              item.filterKeys(_ matches("ex[0-9]_distance")).map(x=>(x._1,x._2.getN.toInt)),
              maybeStepName)
        )
      }
    }


  }

  def getRandomExcercise(maybeStepName:Option[String], distance: Option[Int]) :Future[Exercise]={
    maybeStepName match {
      case Some(stepName)=>{
        val exerciseTableName = s"${stepName}_exercises"
        dynamoDB.getRandomItem(exerciseTableName).map(
          item=>Exercise(
            item.get("name").map(_.getS),
            item.get("display_name").map(_.getS),
            distance,
            maybeStepName))
      }}

    }




  def getAllTrainingSteps():Future[List[Step]]={
    dynamoDB.getAllItems("training_steps").map(
      items=>items.map(
        item=> Step(
          item.get("name").map(_.getS),
          item.get("display_name").map(_.getS),
          item.get("percent").map(_.getN.toInt)) ))
  }

}
