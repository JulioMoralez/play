package controllers


import actors.PaymentReader.Start
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import dao.UserDao.initUsers
import play.api.mvc.{AbstractController, ControllerComponents}

import java.nio.file.Paths
import javax.inject._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}


@Singleton
class FileController @Inject()(@Named("payment-reader") paymentReader: ActorRef, cc: ControllerComponents)
  (implicit system: ActorSystem, ec: ExecutionContext)
  extends AbstractController(cc) {
  def upload = Action(parse.multipartFormData) {implicit request =>
    val duration = 5.seconds
    implicit val timeout: Timeout = Timeout(duration.length, duration.unit)
    initUsers()
//    insertUser(User(Option(-1), Option("Max"), Option(100), email = "eee"))
//    insertUser(User(Option(-1), Option("Bob"), Option(120), email = "eeerrr"))

    request.body
      .file("textfile")
      .map { uploadedFile =>
        val filename    = Paths.get(uploadedFile.filename).getFileName
        val fileSize    = uploadedFile.fileSize
        val contentType = uploadedFile.contentType

        val path = uploadedFile.ref.path

        val future = paymentReader.ask(Start(path, system)).mapTo[Seq[String]]
        Await.result(future, duration)
//        Ok(future.value.get.getOrElse(Nil).mkString("\n"))
        Ok(views.html.index(future.value.get.getOrElse(Nil)))
      }
      .getOrElse {
        Redirect(routes.FirstController.index()).flashing("error" -> "Missing file")
      }
  }
}

