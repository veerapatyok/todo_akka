import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import model.{DatabaseProfile, TodoTable}
import repository.TodoRepo
import route.TodoRoute
import service.TodoService

import scala.concurrent.duration._

object MainServer extends LazyLogging {
  implicit val system = ActorSystem("todo")
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    val shut = CoordinatedShutdown(system)
    val dbProfile = new DatabaseProfile
    val todoTable = new TodoTable(dbProfile.db)
    val todoRepo = new TodoRepo(todoTable)
    val todoService = new TodoService(todoRepo)

    (for {
      _ <- todoRepo.createTable
      bind <- Http().bindAndHandle(new TodoRoute(todoService, dbProfile).route, "0.0.0.0", 8080)
    } yield (bind, shut)).foreach { case (binding, shutdown) =>
      logger.info("start server")

      shutdown.addTask(CoordinatedShutdown.PhaseServiceUnbind, "http-unbind") { () =>
        logger.info("unbind")
        binding.unbind().map(_ => Done)
      }

      shutdown.addTask(CoordinatedShutdown.PhaseServiceRequestsDone, "http-graceful-terminate") { () =>
        logger.info("terminate")
        binding.terminate(10.seconds).map(_ => Done)
      }

      shutdown.addTask(CoordinatedShutdown.PhaseServiceStop, "http-shutdown") { () =>
        logger.info("shutdown")
        Http().shutdownAllConnectionPools().map(_ => Done)
      }
    }
  }
}
