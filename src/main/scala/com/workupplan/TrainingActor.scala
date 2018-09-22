package com.workupplan

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.workupplan.TrainingActor.CreateWorkoutplanRequest
import scala.concurrent.{ExecutionContext, Future}



object TrainingActor {

  def props(trainingService: TrainingService)(implicit executionContext: ExecutionContext) = Props(new TrainingActor(trainingService))

  case class CreateWorkoutplanRequest()

}


case class StepFullData(step:Option[Step],distance: Option[Distance],exercises: Seq[Exercise]=Seq.empty)


class TrainingActor(trainingService: TrainingService)(implicit executionContext: ExecutionContext) extends Actor with ActorLogging {


  override def receive: Receive = {
    case CreateWorkoutplanRequest => {
      log.info(s"TrainingActor#receive#CreateWorkoutplanRequest")
      val eventualSteps = trainingService.getAllTrainingSteps()

      (for {

        steps <- eventualSteps
        distances <- Future.sequence(steps.map(x => {
          trainingService.getRandomDistance(x.name)
        }))
        excercises <- Future.sequence(distances.flatMap(
          distance=>distance.exerciseDistances.map(
            exd=> trainingService.getRandomExcercise(distance.step ,Some(exd._2.toInt))
          )))

      }yield (steps,distances,excercises)).map {

        case (steps,distances,excercises) =>{
          log.info(s"Steps: ${steps}")
          log.info(s"Distances: ${distances}")
          log.info(s"Excercises ${excercises}")
        }
        case _=>log.info("DUPA")

      }
      Actor.emptyBehavior


    }
  }


}
