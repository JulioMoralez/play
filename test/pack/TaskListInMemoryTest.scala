package pack

import org.scalatestplus.play.PlaySpec
import service.TaskListInMemory

class TaskListInMemoryTest extends PlaySpec {
  "TaskListInMemory" must {
    "valid" in {
      TaskListInMemory.validateUser("Max", "1") mustBe(true)
    }

    "invalid" in {
      TaskListInMemory.validateUser("Max", "2") mustBe(false)
    }
  }
}
