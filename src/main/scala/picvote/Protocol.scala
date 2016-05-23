package picvote

import spray.json._
import spray.routing.RequestContext

object Protocol {
  case class Event(`type`: String, payload: String, fromNumber: String, toNumber: String)
  case class PicUrl(url: String)
  case class Pic(name: String, votes: Long)
  case class Report(pics: Vector[Pic])
  case class Vote(pic: String)

  case class SendReport(ctx: RequestContext)

  object Event extends DefaultJsonProtocol {
    implicit val format = jsonFormat4(Event.apply)
  }

  object Pic extends DefaultJsonProtocol {
    implicit val format = jsonFormat2(Pic.apply)
  }

  object Report extends DefaultJsonProtocol {
    implicit val format = jsonFormat1(Report.apply)
  }
}
