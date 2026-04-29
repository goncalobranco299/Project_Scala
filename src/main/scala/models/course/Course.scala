package models.course
import zio.json.{DeriveJsonEncoder,JsonEncoder}

case class Course(id:String, label: String, name: String)

object Course {
  implicit val encoder: JsonEncoder[Course] = DeriveJsonEncoder.gen[Course]
}
