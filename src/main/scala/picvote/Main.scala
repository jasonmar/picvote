package picvote

import scala.concurrent.duration._
import akka.actor._
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.can.Http

object Main extends App {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val actorSystem = ActorSystem()

  val api = actorSystem.actorOf(Props(new RestActor()))

  implicit val executionContext = actorSystem.dispatcher
  implicit val timeout = Timeout(10.seconds)

  IO(Http)
    .ask(Http.Bind(listener = api, interface = host, port = port))
    .mapTo[Http.Event]
    .map{
      case Http.Bound(address) =>
        println(s"REST interface bound to $address")
      case Http.CommandFailed(cmd) =>
        println("REST interface could not bind to " + s"$host:$port, ${cmd.failureMessage}")
        actorSystem.terminate()
    }
}
