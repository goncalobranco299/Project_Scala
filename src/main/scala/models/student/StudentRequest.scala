package models.student
import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class StudentRequest(name: String, email: String, studentNumber: String, classId: String)

object StudentRequest {
  implicit val decoder: JsonDecoder[StudentRequest] = DeriveJsonDecoder.gen[StudentRequest]
}
