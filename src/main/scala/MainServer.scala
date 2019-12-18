import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import route.Hello

import scala.concurrent.duration._

object MainServer extends LazyLogging {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("ghostring")
    implicit val executionContext = system.dispatcher

    val binding = Http().bindAndHandle(Hello.route, "0.0.0.0", 8080)

    binding.foreach(_ => logger.info("start server"))

    val shutdown = CoordinatedShutdown(system)

    shutdown.addTask(CoordinatedShutdown.PhaseServiceUnbind, "http-unbind") { () =>
      logger.info("unbind")
      binding.flatMap(_.unbind().map(_ => Done))
    }

    shutdown.addTask(CoordinatedShutdown.PhaseServiceRequestsDone, "http-graceful-terminate") { () =>
      logger.info("terminate")
      binding.flatMap(_.terminate(10.seconds).map(_ => Done))
    }

    shutdown.addTask(CoordinatedShutdown.PhaseServiceStop, "http-shutdown") { () =>
      logger.info("shutdown")
      Http().shutdownAllConnectionPools().map(_ => Done)
    }
  }
}
