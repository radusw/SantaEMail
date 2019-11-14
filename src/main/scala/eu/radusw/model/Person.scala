package eu.radusw.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

final case class Person(email: String, name: String)
object Person {
  implicit val decoder: Decoder[Person] = deriveDecoder
  implicit val encoder: Encoder[Person] = deriveEncoder
}