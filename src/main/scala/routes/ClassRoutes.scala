package routes
import zio._
import zio.json._
import zio.http._
import zio.http.model._
import models.classe.ClassRequest
import services.classe.ClassService

object ClassRoutes {

val app: HttpApp[ClassService, Throwable] = Http.collectZIO[Request] {

  // List all class (GET)
  case Method.GET -> !! / "class" =>
    for {
      service <- ZIO.service[ClassService]
      list <- service.getClasses.tapError(e => ZIO.logError(e.getMessage))
    } yield Response.json(list.toJson)

  // Create a class (POST)
  case req @ Method.POST -> !! / "class" =>
    for {
      body    <- req.body.asString
      request <- ZIO.fromEither(body.fromJson[ClassRequest])
        .mapError(e => new Exception(s"Invalid JSON: $e"))
      service <- ZIO.service[ClassService]
      msg     <- service.postClass(request.name,request.number)
    } yield Response.text(msg)
}
  }
