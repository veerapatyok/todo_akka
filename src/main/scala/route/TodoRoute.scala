package route

import java.util.UUID

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import model.DatabaseProfile
import message.todo.TodoPost
import message.todo.TodoPostJson
import message.todo.TodoPostJson._
import service.TodoService

import scala.util.{Failure, Success}

class TodoRoute(todoService: TodoService,
                dbProfile: DatabaseProfile) {
  val route = {
    path("todos") {
      get {
        import message.todo.TodoJson._

        onComplete(todoService.getAllTodo(dbProfile)) {
          case Success(v) => complete(v)
          case Failure(e) => complete(500, e.getMessage)
        }
      } ~
        (post & entity(as[TodoPost])) { todo =>
          onComplete(todoService.insert(TodoPostJson.toTodoModel(todo, UUID.randomUUID().toString))(dbProfile)) {
            case Success(value) => complete(value)
            case Failure(exception) => complete(500, exception.getMessage)
          }
        }
    }
  }
}
