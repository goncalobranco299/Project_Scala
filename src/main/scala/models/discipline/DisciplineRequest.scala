package models.discipline
import models.teacher.TeacherRequest
import zio.json.{DeriveJsonDecoder, JsonDecoder}


case class DisciplineRequest(name: String, hours: Int, teacherId: String, teacherRequest: TeacherRequest)


object DisciplineRequest {
  implicit val decoder: JsonDecoder[DisciplineRequest] = DeriveJsonDecoder.gen[DisciplineRequest]

}
