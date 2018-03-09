import com.google.inject.{Inject, Provider, Singleton}
import play.api.http.{ContentTypes, DefaultHttpErrorHandler}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{RequestHeader, Result, Results}
import play.api.routing.Router
import play.api.{Configuration, Environment, OptionalSourceMapper}

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(
                              env: Environment,
                              config: Configuration,
                              sourceMapper: OptionalSourceMapper,
                              router: Provider[Router]
                            )
  extends DefaultHttpErrorHandler(env, config, sourceMapper, router)
    with ContentTypes
    with Results {


  override protected def onBadRequest(request: RequestHeader, message: String): Future[Result] =
    request.contentType.collect {
      case JSON => BadRequest(Json.toJson(Message("error", message)))
    }.fold(super.onBadRequest(request, message))(Future.successful)

}

case class Message(status: String, message: String)

object Message {
  implicit val format: OFormat[Message] = Json.format
}
