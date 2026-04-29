package models.course
import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class CourseRequest(name: String)

object CourseRequest{
  implicit val decoder: JsonDecoder[CourseRequest] = DeriveJsonDecoder.gen[CourseRequest]
}
