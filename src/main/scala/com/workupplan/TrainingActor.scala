package com.workupplan

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.workupplan.TrainingActor.CreateWorkoutplanRequest
import com.workupplan.dynamodb.DynamoDB

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


object TrainingActor {

  def props(dynamoDBClient: DynamoDB)(implicit executionContext: ExecutionContext) = Props(new TrainingActor(dynamoDBClient))

  case class CreateWorkoutplanRequest()

}

case class Step(name: String, displayName: String, percent: Int)

case class StepDistance(stepName: String, distance: Int)

case class Exercise(stepName: String, name: String, displayName: String, exerciseDistance: Int)

class TrainingActor(dynamoDBClient: DynamoDB)(implicit executionContext: ExecutionContext) extends Actor with ActorLogging {

  var steps: Seq[Step] = Seq.empty
  var stepsDistances: Seq[StepDistance] = Seq.empty
  var exercises: Seq[Exercise] = Seq.empty


  override def receive: Receive = {
    case CreateWorkoutplanRequest => {
      log.info(s"TrainingActor#receive#CreateWorkoutplanRequest")

      dynamoDBClient.getAllItems("training_steps") onComplete {
        case Success(trainingSteps) => {
          trainingSteps.foreach(composeTrainingStepData(_))
        }

        case Failure(t) => log.error("Problem with fetching steps ", t)
      }
      Actor.emptyBehavior


    }
  }


  private def composeTrainingStepData(step: Map[String, AttributeValue]) = {

    log.info(s"Processing the step ${step}")
    step.get("name").foreach(stepNameAttribiute => {

      val stepName = stepNameAttribiute.getS
      log.info(s"Get distances for step ${stepName}")
      val distanceTableName = s"${stepName}_distances"

      dynamoDBClient.getRandomItem(distanceTableName) onComplete {
        case Success(distance) => {
          log.info(s"Processing random distance ${distance} for ${stepName}")
          val exerciseTableName = s"${stepName}_exercises"
          val pattern = raw"ex[0-9]_distance".r
          distance.foreach(x => x._1 match {
            case pattern(_*) => {
              dynamoDBClient.getRandomItem(exerciseTableName) onComplete {
                case Success(exercise) => {
                  log.info(s"Processing exercise ${exercise} for distance ${distance} and  step ${stepName}")
                }
                case Failure(t) => log.error(s"Problem with fetching random exercise for ${stepName} and ${distance}", t)
              }
            }
            case _ => Map.empty
          })
        }
        case Failure(t) => log.error(s"Problem with fetching random distance for ${stepName}", t)
      }

    })
  }

}
