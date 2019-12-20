package message

import message.todo.{Done, Todo}
import io.circe.generic.auto._
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.flatspec.AsyncFlatSpec
import io.circe.syntax._

class TodoSpec extends AsyncFlatSpec with GivenWhenThen with BeforeAndAfterAll {
  "Todo case class" should "have custom encode with status case class" in {
    import message.todo.TodoJson._

    Given("mock data")
    val fakeTodo = Todo("id", "test", Done)

    When("encode to json")
    val result = fakeTodo.asJson

    Then("data should encode and return status")
    assert(result.\\("status").headOption.map(_.asString).contains(Some("done")))
  }
}
