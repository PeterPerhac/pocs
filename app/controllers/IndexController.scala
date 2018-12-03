package controllers

import javax.inject.Inject
import models.Person
import play.api.mvc._
import repository.MongoRepository

class IndexController @Inject()(mongoRepository: MongoRepository)(ds: CommonDependencies) extends BaseController(ds) {

  def show = Action(Ok(views.html.homePage()))

  val foo = Action(implicit req => Ok(views.html.people(Seq(Person("Peter", 33), Person("Elizabeth", 7)))))

}
