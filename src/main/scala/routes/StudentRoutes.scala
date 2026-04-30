package routes
import zio._
import zio.json._
import zio.http._
import zio.http.model._
import models.student.StudentRequest
import services.student.StudentService

object StudentRoutes {

val app: HttpApp[StudentService, Throwable] = Http.collectZIO[Request] {

  // List all students (GET)
  case Method.GET -> !! / "student" =>
    for {
      service <- ZIO.service[StudentService]
      list    <- service.getStudents.tapError(e => ZIO.logError(e.getMessage))
    } yield Response.json(list.toJson)

  // Create a student (POST)
  case req @ Method.POST -> !! / "student" =>
    for {
      body    <- req.body.asString
      request <- ZIO.fromEither(body.fromJson[StudentRequest])
        .mapError(e => new Exception(s"Invalid JSON: $e"))
      service <- ZIO.service[StudentService]
      msg  <- service.postStudent(request, request.classId)
    } yield Response.text(msg)
  }
}
