package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}
import repository.MongoRepository

class PeopleController @Inject()(mongoRepository: MongoRepository)(ds: CommonDependencies) extends BaseController(ds) {

  import scala.concurrent.ExecutionContext.Implicits.global

  val generateSomePeople: Action[AnyContent] =
    Action.async(mongoRepository.generate().map(n => Ok(n.toString)))

  val listPeople: Action[AnyContent] =
    Action.async { implicit req =>
      mongoRepository
        .listAllPeople()
        .map(p =>
          render {
            case Accepts.Json() => Ok(Json.toJson(p))
            case Accepts.Html() => Ok(views.html.people(p))
            case _              => Ok(p.toString)
        })
    }

}
