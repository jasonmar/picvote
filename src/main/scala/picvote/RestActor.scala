package picvote

import spray.routing._

class RestActor extends HttpServiceActor with RestApi {
  def receive = runRoute(routes)
}

