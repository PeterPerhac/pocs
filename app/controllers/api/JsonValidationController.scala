package controllers.api

import javax.inject.Inject

import controllers.{BaseController, CommonDependencies}
import models._
import play.api.libs.json.Json
import play.api.mvc.Action

class JsonValidationController @Inject()(ds: CommonDependencies) extends BaseController(ds) {

  def list: Action[Unit] =
    Action(parse.empty)(_ => Ok(Json.toJson(Car("Ford", "Focus", 2008))))

  def validate: Action[Car] =
    Action(parse.json[Car])(req => Ok(Json.toJson(req.body)))

}
