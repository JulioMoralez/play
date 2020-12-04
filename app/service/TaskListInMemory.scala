package service



object TaskListInMemory {
  private val users = scala.collection.mutable.Map[String, String]("Max" -> "1", "Sam" -> "2")
  private val tasks = scala.collection.mutable.Map[String, List[String]]("Max" -> List("11", "bb", "33"))

  def validateUser(username: String, password: String): Boolean = {
    users.get(username).map(_ == password).getOrElse(false)
  }

  def createUser(username: String, password: String): Boolean = {
    if (users.contains(username)) false else {
      users(username) = password
      true
    }
  }

  def getTasks(username: String): Seq[String] = {
    tasks.getOrElse(username, Nil)
  }

  def addTask(username: String, task: String): Unit = {
    tasks(username) = task :: tasks.getOrElse(username, Nil)
  }

  def removeTask(username: String, index: Int): Boolean = {
    if (index < 0 || tasks(username).isEmpty || index > tasks(username).length) false else {
      tasks(username) = tasks(username).patch(index, Nil, 1)
      true
    }
  }
}

