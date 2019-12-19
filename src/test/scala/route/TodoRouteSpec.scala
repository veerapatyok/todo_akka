package route

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.syntax._
import message.todo.TodoPostJson._
import message.todo.{Done, TodoPost}
import model.{DatabaseProfile, TodoTable}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import repository.TodoRepo
import service.TodoService

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language._

class TodoRouteSpec extends AsyncFlatSpec
  with GivenWhenThen
  with BeforeAndAfterAll
  with Matchers
  with ScalatestRouteTest {

  val dbProfile = new DatabaseProfile
  val todoTable = new TodoTable(dbProfile)
  val todoRepo = new TodoRepo(todoTable)
  val todoService = new TodoService(todoRepo)
  val todoRoute = new TodoRoute(todoService, dbProfile)

  override def beforeAll(): Unit = Await.result(todoService.createTable(dbProfile), 5 seconds)

  override def afterAll(): Unit = dbProfile.db.db.close()

  "/todos with POST" should "insert task" in {
    Given("make fake data and request")
    val fakeTodo = TodoPost("test", Done).asJson.noSpaces
    val postRequest = HttpRequest(
      HttpMethods.POST,
      uri = "/todos",
      entity = HttpEntity(MediaTypes.`application/json`, fakeTodo))

    When("call todos path with POST")
    postRequest ~> todoRoute.route ~> check {
      Then("return ok")
      response.status should be(StatusCodes.OK)
    }
  }

  "/todos with GET" should "get all task" in {
    Given("nothing")
    
    When("call todos path with GET")
    Get("/todos") ~> todoRoute.route ~> check {
      Then("return ok")
      response.status should be(StatusCodes.OK)
    }
  }

}
