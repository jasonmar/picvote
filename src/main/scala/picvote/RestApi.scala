package picvote

import akka.actor._
import akka.routing.RoundRobinPool
import akka.util.Timeout
import picvote.Protocol.{Event, PicUrl, SendReport, Vote}
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport
import spray.routing._

import scala.concurrent.duration._

trait RestApi extends HttpService with SprayJsonSupport with ActorLogging {actor: Actor =>
  implicit val timeout = Timeout(10.seconds)

  private val voteActor = context.actorOf(Props(new VoteActor)) // increments votes
  private val picActors = context.actorOf(Props(new PicActor).withRouter(RoundRobinPool(5))) // saves pic to dropbox
  val r = scala.util.Random

  def routes: Route = {
    path("report"){
      get {ctx =>
        voteActor ! SendReport(ctx)
      }
    } ~
    path("event"){id =>
      post {
        entity(as[Event]){event => ctx =>
          if (event.`type` == "inboundText"){
            voteActor ! Vote(event.payload)
            ctx.complete(StatusCodes.Accepted)
          } else if (event.`type` == "inboundMedia"){
            picActors ! PicUrl(event.payload)
            ctx.complete(StatusCodes.Accepted)
          } else {
            ctx.complete(StatusCodes.BadRequest)
          }
        }
      }
    }
  }
}
