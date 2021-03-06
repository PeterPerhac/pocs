package utils

import java.time.LocalDate

import models.DateComponents
import org.apache.commons.lang3.StringUtils
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import play.api.data.{FieldMapping, FormError, Forms}

import scala.util.{Failure, Success}

object ValidationUtils {

  implicit val mandatoryBooleanFormatter: Formatter[Boolean] = new Formatter[Boolean] {

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Boolean] =
      Right(data.getOrElse(key, "")).right.flatMap {
        case "true"  => Right(true)
        case "false" => Right(false)
        case _       => Left(Seq(FormError(key, s"$key.error.boolean", Nil)))
      }

    def unbind(key: String, value: Boolean): Map[String, String] = Map(key -> value.toString)
  }

  val mandatoryBoolean: FieldMapping[Boolean] = Forms.of[Boolean]
  val notBlank: String => Boolean = StringUtils.isNotBlank

  def unconstrained[T]: Constraint[T] = Constraint(_ => Valid)

  def inRange[T](minValue: T, maxValue: T, errorCode: String = "")(
        implicit ordering: scala.math.Ordering[T]): Constraint[T] =
    Constraint { t =>
      assert(ordering.compare(minValue, maxValue) < 0, "min bound must be less than max bound")
      (ordering.compare(t, minValue).signum, ordering.compare(t, maxValue).signum) match {
        case (1, -1) | (0, _) | (_, 0) => Valid
        case (_, 1)                    => Invalid(ValidationError(s"error$errorCode.range.above", maxValue))
        case (-1, _)                   => Invalid(ValidationError(s"error$errorCode.range.below", minValue))
      }
    }

  def validDate(constraint: Constraint[LocalDate] = unconstrained): Constraint[DateComponents] =
    Constraint[DateComponents] { dcs: DateComponents =>
      DateComponents.toLocalDate(dcs) match {
        case Failure(_)         => Invalid(ValidationError("error.date.invalid", dcs))
        case Success(localDate) => constraint(localDate)
      }
    }

  def optionallyMatchingPattern(regex: String): Constraint[String] =
    Constraint[String] { s: String =>
      Option(s) match {
        case None | Some("")       => Valid
        case _ if s.matches(regex) => Valid
        case _                     => Invalid(ValidationError("error.string.pattern", s))
      }
    }

}
