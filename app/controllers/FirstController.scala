package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import service.TaskListInMemory

@Singleton
class FirstController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def taskList1: Action[AnyContent] = Action { implicit request =>
    val username = request.session.get("username")
    username match {
      case Some(username) =>
        val tasks = TaskListInMemory.getTasks(username)
        Ok(views.html.first(tasks))
      case None => Redirect(routes.FirstController.login())
    }

  }

  def login: Action[AnyContent] = Action { implicit request => {
    Ok(views.html.login1())
  }
  }

  def index: Action[AnyContent] = Action { implicit request => {
    Ok(views.html.index(Nil))
  }
  }

  def validateLoginGet(username: String, password: String): Action[AnyContent] = Action {
    Ok(s"username = $username, password = $password")
  }

  def validateLoginPost: Action[AnyContent] = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemory.validateUser(username, password)) {
        Redirect(routes.FirstController.taskList1()).withSession("username" -> username)
      } else {
        Redirect(routes.FirstController.login()).flashing("error" -> "invalid password")
      }
    }.getOrElse(Redirect(routes.FirstController.login()))
  }

  def createUser: Action[AnyContent] = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemory.createUser(username, password)) {
        Redirect(routes.FirstController.taskList1()).withSession("username" -> username)
      } else {
        Redirect(routes.FirstController.login()).flashing("error" -> "user exist")
      }
    }.getOrElse(Redirect(routes.FirstController.login()))
  }

  def logout: Action[AnyContent] = Action {
    Redirect(routes.FirstController.login()).withNewSession
  }

  def addTask(): Action[AnyContent] = Action { implicit request =>
    val username = request.session.get("username")
    username match {
      case Some(username) =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args =>
          val task = args("task").head
          TaskListInMemory.addTask(username, task)
          Redirect(routes.FirstController.taskList1())
        }.getOrElse(Redirect(routes.FirstController.taskList1()))
      case None => Redirect(routes.FirstController.taskList1())
    }
  }

  def removeTask():Action[AnyContent] = Action { implicit request =>
    val username = request.session.get("username")
    username match {
      case Some(username) =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args =>
          val index = args("index").head.toInt
          TaskListInMemory.removeTask(username, index)
          Redirect(routes.FirstController.taskList1())
        }.getOrElse(Redirect(routes.FirstController.taskList1()))
      case None => Redirect(routes.FirstController.taskList1())
    }
  }

  def withArgs(name: String, age: Int): Action[AnyContent] = Action {
    Ok(s"Name = $name, age = $age")
  }


}
