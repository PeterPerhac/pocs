package controllers

import akka.stream.Materializer
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}

class FileUploadController @Inject()(ds: CommonDependencies)(implicit materializer: Materializer)
    extends BaseController(ds) {

  val UPLOAD_ERROR = "uploadError"

  val accept = Action(parse.maxLength(100000, parse.multipartFormData)) { implicit request =>
    request.body.fold(
      maxExceeded => Redirect(routes.FileUploadController.show()).flashing(UPLOAD_ERROR -> "upload.fileTooLarge"),
      multipart => Ok(s"We have your file. ${multipart.files.map(_.ref.file.length()).head} bytes")
    )
  }

  val show: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.fileupload(request.flash.get(UPLOAD_ERROR)))
  }

}
