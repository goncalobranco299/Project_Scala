package models.teacher

import models.discipline.DisciplineRequest
import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class TeacherRequest(name: String, discipline: DisciplineRequest)

object TeacherRequest {
  implicit val decoder: JsonDecoder[TeacherRequest] = DeriveJsonDecoder.gen[TeacherRequest]
}
