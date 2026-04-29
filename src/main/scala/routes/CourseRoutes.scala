package routes
import zio._
import zio.json._
import zio.http._
import zio.http.model._
import models.course.CourseRequest
import services.course.CourseService

object CourseRoutes {

  val app: HttpApp[CourseService, Throwable] = Http.collectZIO[Request] {

    // List all class (GET)
    case Method.GET -> !! / "class" =>
      for {
        service <- ZIO.service[CourseService]
        list <- service.getCourse.tapError(e => ZIO.logError(e.getMessage))
      } yield Response.json(list.toJson)

    // Create a class (POST)
    case req @ Method.POST -> !! / "class" =>
      for {
        body    <- req.body.asString
        request <- ZIO.fromEither(body.fromJson[CourseRequest])
          .mapError(e => new Exception(s"Invalid JSON: $e"))
        service <- ZIO.service[CourseService]
        msg     <- service.postCourse(request.name)
      } yield Response.text(msg)
  }
}