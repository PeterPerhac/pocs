package repository

import javax.inject.Inject
import models.Person
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class MongoRepository @Inject()(mongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) {

  import play.modules.reactivemongo.json._

  private val eh = Cursor.DoneOnError[Vector[Person]]()

  private implicit lazy val peopleCollection: Future[JSONCollection] =
    mongoApi.database.map(_.collection[JSONCollection]("people"))

  def generate(): Future[Int] =
    peopleCollection.flatMap { coll =>
      val people = (1 to 10).toList.map(_ => Person.gen.sample.get)
      Future.traverse(people)(p => coll.insert(p)).map(_.size)
    }

  def listAllPeople(): Future[Vector[Person]] =
    peopleCollection.flatMap(
      _.find(Json.obj()).cursor[Person](ReadPreference.Primary).collect[Vector](1000, eh)
    )

}
