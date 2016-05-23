package picvote

import akka.actor.{Actor, ActorLogging}
import picvote.Protocol.{Pic, Report, SendReport, Vote}
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport

class VoteActor extends Actor with SprayJsonSupport with ActorLogging {

  val state = collection.mutable.Map[String,Long]()

  def receive = {
    case Vote(picName) =>

      // TODO only allow votes for pics have been uploaded for voting
      state.get(picName) match {
        case Some(votes) =>
          state.put(picName, votes + 1)
        case _ =>
          state.put(picName, 1)
      }

    case SendReport(ctx) =>
      val pics = state.map(t => Pic(t._1, t._2)).toVector
      ctx.complete(StatusCodes.OK, Report(pics))

  }

}