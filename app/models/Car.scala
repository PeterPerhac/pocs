package models

import play.api.libs.json.{Json, OFormat}

case class Car(make: String, model: String, year: Int, color: Option[String] = None)

object Car{
  implicit val format: OFormat[Car] = Json.format[Car]
}
