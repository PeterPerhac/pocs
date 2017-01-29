package controllers

import javax.inject.Inject

import org.apache.commons.codec.binary.Base64.decodeBase64
import play.api.Configuration
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future


abstract class BaseController(val ds: CommonDependencies) extends Controller with I18nSupport with WSSupport {

  protected lazy val ws = ds.ws
  protected lazy val conf = ds.conf
  implicit lazy val messagesApi = ds.messagesApi

  def BasicSecured[A](action: Action[A]): Action[A] = Action.async(action.parser) { request =>
    val submittedCredentials: Option[List[String]] = for {
      authHeader <- request.headers.get("Authorization")
      parts <- authHeader.split(' ').drop(1).headOption
    } yield new String(decodeBase64(parts.getBytes)).split(":").toList

    submittedCredentials.collect {
      case u :: p :: Nil if u == "admin" && p == "foofoo" =>
    }.map(_ => action(request)).getOrElse {
      Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured Area""""))
    }
  }

}

final class CommonDependencies @Inject()(val ws: WSClient, val conf: Configuration, val messagesApi: MessagesApi)
