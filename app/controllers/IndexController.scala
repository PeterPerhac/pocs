package controllers

import javax.inject.Inject

import play.api.mvc._

class IndexController @Inject()(ds: CommonDependencies) extends BaseController(ds) {

  def show = Action(Ok(views.html.homePage()))

}
