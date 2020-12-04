package controllers

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import controllers.ConfiguredActor.GetConfig
import play.api.Configuration
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ConfiguredActor {
  case object GetConfig
}

class ConfiguredActor @Inject() (configuration: Configuration) extends Actor {

  val config: String = configuration.getOptional[String]("my.config").getOrElse("none")

  def receive: Receive = {
    case GetConfig =>
      sender() ! config
  }
}



@Singleton
class AkkaController @Inject() (@Named("configured-actor") configuredActor: ActorRef, components: ControllerComponents)(
  implicit ec: ExecutionContext
) extends AbstractController(components) {
  implicit val timeout: Timeout = 5.seconds


//  def getConfig: Action[AnyContent] = Action {
//    Ok("123")
//  }
  def getConfig: Action[AnyContent] = Action.async {
    (configuredActor ? GetConfig).mapTo[String].map { message =>
      Ok(message)
    }
  }
}
