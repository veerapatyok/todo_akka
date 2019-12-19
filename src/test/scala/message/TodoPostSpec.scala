package message

import message.todo.{Done, TodoPost}
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.flatspec.AsyncFlatSpec
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._

class TodoPostSpec extends AsyncFlatSpec with GivenWhenThen with BeforeAndAfterAll {
  "TodoPost case class" should "have custom encode with status case class" in {
    import message.todo.TodoPostJson._

    Given("mock data")
    val fakeTodo = TodoPost("test", Done)

    When("encode to json")
    val result = fakeTodo.asJson

    Then("data should encode and return status")
    assert(result.\\("status").headOption.map(_.asString).contains(Some("done")))
  }

  "TodoPost case class" should "have custom decode with status case class" in {
    import message.todo.TodoPostJson._

    Given("mock data")
    val fakeTodo =
      """
        |{
        | "task": "test"
        | "status": "done"
        |}
        |""".stripMargin

    When("encode to json")
    val result = decode[TodoPost](fakeTodo)

    Then("data should decode and return status")
    assert(result.isRight)
  }
}
