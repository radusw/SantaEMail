package eu.radusw.model

import io.circe.{Decoder, Encoder}

final case class Error(msg: String) extends AnyVal
object Error {
  implicit val encoder: Encoder[Error] = Encoder.encodeString.contramap[Error](_.msg)
  implicit val decoder: Decoder[Error] = Decoder.decodeString.map(Error(_))
}
