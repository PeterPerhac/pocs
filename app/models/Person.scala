package models

import play.api.libs.json.{Json, OFormat}
import org.scalacheck.Gen
import org.scalacheck.Gen._

case class Person(name: String, age: Int)

object Person {
  implicit val format: OFormat[Person] = Json.format
  val nameGen:Gen[String] = oneOf("Peter", "Tom", "John", "Jim", "Paul", "Tim")
  val gen : Gen[Person] = for {
    name <- nameGen
    age <- choose(12,99)
  } yield Person(name, age)
}