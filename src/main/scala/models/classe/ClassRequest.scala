package models.classe

import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class ClassRequest(name: String,number: String)


object ClassRequest {
  implicit val decoder: JsonDecoder[ClassRequest] = DeriveJsonDecoder.gen[ClassRequest]
}
