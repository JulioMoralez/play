package model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

case class User (id: Option[Int], name: Option[String], balance: Option[Int], email: String)

class Users(tag: Tag) extends Table[User](tag, "T_USERS2") {
  def id             = column[Int]("USER_ID", O.PrimaryKey, O.AutoInc)
  def name           = column[Option[String]]("USER_NAME", O.Length(64))
  def balance        = column[Option[Int]]("USER_BALANCE")
  def email          = column[String]("USER_EMAIL", O.Length(512))
  def nameIndex      = index("USER_NAME_IDX", name, unique = true)
  override def * : ProvenShape[User] = (id.?, name, balance, email) <> (User.tupled, User.unapply)
}
