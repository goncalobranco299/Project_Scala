package models.discipline
import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class Discipline(id: String, label: String, name: String, hours: Int)


object Discipline {
  implicit val encoder: JsonEncoder[Discipline] =  DeriveJsonEncoder.gen[Discipline]
}
