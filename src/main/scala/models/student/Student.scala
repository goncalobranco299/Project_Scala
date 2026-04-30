package models.student
import zio.json.{DeriveJsonEncoder, JsonEncoder}

  case class Student(id: String, label: String, name: String, email: String, studentNumber: String, classId: String)

  object Student {
    implicit val encoder: JsonEncoder[Student] = DeriveJsonEncoder.gen[Student]
  }

