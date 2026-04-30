package models.teacher
import models.discipline.Discipline
import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class Teacher(id: String, label: String, name: String, discipline: Option[Discipline])

object Teacher {
  implicit val encoder: JsonEncoder[Teacher] = DeriveJsonEncoder.gen[Teacher]

}

