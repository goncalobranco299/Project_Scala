package models.discipline
import zio.json.{DeriveJsonDecoder, JsonDecoder}


case class DisciplineRequest(name: String, hours: Int)


object DisciplineRequest {
  implicit val decoder: JsonDecoder[DisciplineRequest] = DeriveJsonDecoder.gen[DisciplineRequest]

}
