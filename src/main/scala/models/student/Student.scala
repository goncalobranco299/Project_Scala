package models.student
import zio.json.{DeriveJsonEncoder, JsonEncoder}

  case class Student(id: String, label: String, name: String, age: Int)

  object Student {
    implicit val encoder: JsonEncoder[Student] = DeriveJsonEncoder.gen[Student]
  }

