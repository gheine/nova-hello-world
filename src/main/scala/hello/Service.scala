package hello

import org.http4s.HttpService
import org.http4s.dsl._
import org.http4s.server.ServerApp
import org.http4s.server.blaze.BlazeBuilder
import scalaz.stream.Process

object Service extends ServerApp {

  def busy(n: Int): Process[Nothing, String] = {
    var dummy = 0
    Process.emitAll(0 to n).map { _ =>
      val threads = 1 to Runtime.getRuntime.availableProcessors map { _ =>
        val t = new Thread() {
          override def run = 0 to 1000000000 foreach (dummy = _)
        }
        t.start()
        t
      }
      threads.foreach(_.join)
      "1\r\n.\r\n"
    }
  }

  val routes = HttpService {
    case GET -> Root / "_ping_" => Ok("pong")
    case HEAD -> Root / "_ping_" => Ok()
    case GET -> Root / "hello" / name => Ok(s"Hello $name!")
    case GET -> Root / "busy" / IntVar(n) => Ok(busy(n))
  }

  override def server(args: List[String]) = {
    BlazeBuilder
      .bindHttp(8080, "0.0.0.0")
      .mountService(routes)
      .start
  }
}

