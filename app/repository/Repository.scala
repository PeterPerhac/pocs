package repository

import com.google.inject.ImplementedBy
import models.Reading

import scala.concurrent.Future

@ImplementedBy(classOf[MongoRepository])
trait Repository {

  def findAll(r: String): Future[Vector[Reading]]

}
