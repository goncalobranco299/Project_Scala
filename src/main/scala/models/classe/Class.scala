 package models.classe
import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class Class(id: String, label: String, name: String,number: String)

object Class {
  implicit val encoder: JsonEncoder[Class] = DeriveJsonEncoder.gen[Class]
}


