package repository

import javax.inject.Inject

import models.Reading
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

class MongoRepository @Inject()(mongoApi: ReactiveMongoApi) extends Repository {

  import play.modules.reactivemongo.json._

  import scala.concurrent.ExecutionContext.Implicits.global

  private implicit lazy val collection = mongoApi.database.map(_.collection[JSONCollection]("readings"))

  private def execute[T](op: JSONCollection => Future[T])(implicit coll: Future[JSONCollection]) = coll flatMap op

  def findAll(r: String): Future[Vector[Reading]] = {
    execute {
      _.find(BSONDocument("reg" -> r)).cursor[Reading](ReadPreference.Primary)
        .collect[Vector](1000, Cursor.DoneOnError[Vector[Reading]]())
    }
  }

}