package route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import model.{H2Profile, TodoTable}
import message.todo.TodoPost
import message.todo.TodoPostJson
import message.todo.TodoPostJson._

import scala.util.{Failure, Success}

class TodoRoute(todoTable: TodoTable) {
  val route = {
    path("todos") {
      get {
        import message.todo.TodoJson._

        onComplete(H2Profile.db.db.run(todoTable.getAll)) {
          case Success(v) => complete(v)
          case Failure(e) => complete(500, e.getMessage)
        }
      } ~
        (post & entity(as[TodoPost])) { todo =>
          onComplete(H2Profile.db.db.run(todoTable.insert(TodoPostJson.toTodoModel(todo)))) {
            case Success(value) => complete(value)
            case Failure(exception) => complete(500, exception.getMessage)
          }
        }
    }
  }
}
