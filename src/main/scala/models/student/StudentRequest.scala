package models.student
import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class StudentRequest(name: String, age: Int)

object StudentRequest {
  implicit val decoder: JsonDecoder[StudentRequest] = DeriveJsonDecoder.gen[StudentRequest]
}
