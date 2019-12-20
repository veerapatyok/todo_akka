package service

import java.util.UUID

import message.todo.{Done, Todo}
import model.{DatabaseProfile, TodoTable}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import repository.TodoRepo

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language._

class TodoServiceSpec extends AsyncFlatSpec with GivenWhenThen with BeforeAndAfterAll {
  val dbProfile = new DatabaseProfile
  val todoTable = new TodoTable(dbProfile)
  val todoRepo = new TodoRepo(todoTable)
  val todoService = new TodoService(todoRepo)

  override def beforeAll(): Unit = Await.result(todoService.createTable(dbProfile), 5 seconds)

  override def afterAll(): Unit = dbProfile.db.db.close()

  "todo service" should "insert data" in {
    Given("make fake data")
    val fakeTodo = Todo(UUID.randomUUID().toString, "test", Done)

    When("call insert")
    val result = todoService.insert(fakeTodo)(dbProfile)

    Then("return success")
    result.map(r => assert(r == 1))
  }

  "todo service" should "get all data" in {
    Given("make fake data")
    val fakeTodo = Todo(UUID.randomUUID().toString, "test", Done)

    When("call insert and call get all")
    val result = for {
      _ <- todoService.insert(fakeTodo)(dbProfile)
      todos <- todoService.getAllTodo(dbProfile)
    } yield todos

    Then("return success")
    result.map(r => assert(r.length == 2))
  }
}
