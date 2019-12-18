import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.server.Directives._
import model.h2.{H2Profile, TodoTable}
import route.{Hello, TodoRoute}

import scala.concurrent.duration._

object MainServer extends LazyLogging {
  implicit val system = ActorSystem("ghostring")
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    val shut = CoordinatedShutdown(system)
    val todoTable = new TodoTable(H2Profile.db)

    (for {
      _ <- todoTable.createTable
      bind <- Http().bindAndHandle(Hello.route ~ new TodoRoute(todoTable).route, "0.0.0.0", 8080)
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
