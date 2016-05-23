package picvote

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse, OAuth2BearerToken}
import spray.http.HttpHeaders.Authorization

import scala.concurrent.Future
import picvote.Protocol._

class PicActor extends Actor {

  import context.dispatcher
  val config = ConfigFactory.load()
  private val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
  val dropBoxFolder = "/" + config.getString("dropbox.foldername") + "/"
  val dropBoxBearerToken = config.getString("dropbox.authtoken")
  val dropBoxAuthHeader = Authorization(OAuth2BearerToken(dropBoxBearerToken))
  def dropBoxUrl(filename: String): String = "https://content.dropboxapi.com/1/files_put/auto" + dropBoxFolder + filename + "?overwrite=false&autorename=false"

  def getPicFromBurner(url: String) = pipeline(Get(url))
  def uploadPicToDropBox(bytes: Array[Byte], filename: String) = pipeline(Put(dropBoxUrl(filename), bytes).withHeaders(dropBoxAuthHeader))

  def receive = {
    case PicUrl(url) =>
      val filename = url.split("/").last

      // Download the file from Burner
      for (
        response <- getPicFromBurner(url);
        upload <- uploadPicToDropBox(response.entity.data.toByteArray, filename)
      ) yield upload.status
  }

}