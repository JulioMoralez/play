package dao

import model.{User, Users}
import slick.dbio.DBIOAction
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UserDao {

  lazy val users: TableQuery[Users] = TableQuery[Users]

  val db = Database.forConfig("mydb")
  println(db.source.maxConnections.get)

  def initUsers(): Future[Unit] = db.run(DBIOAction.seq(users.schema.create))

  def insertUser(user: User): Future[User] = db
    .run(users returning users.map(_.id) += user)
    .map(id => user.copy(id = Some(id)))

  def findUser(name: String): Future[Option[User]] = db.run(users.filter(_.name === name).result.headOption)

  def updateUser(newuser: User): Future[Boolean] = {
    val query = for (user <- users if user.id === newuser.id)
      yield user
    db.run(query.update(newuser)).map(_ > 0)
  }

  def updateUserBalance(name: String, balance: Int): Future[Boolean] = {
    val query = for (user <- users if user.name === name)
      yield user.balance
    db.run(query.update(Option(balance))).map(_ > 0)
  }
}
