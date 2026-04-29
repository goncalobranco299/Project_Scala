package routes
import zio._
import zio.json._
import zio.http._
import zio.http.model._
import models.discipline.DisciplineRequest
import services.discipline.DisciplineService

object DisciplineRoutes {

  val app: HttpApp[DisciplineService, Throwable] = Http.collectZIO[Request] {

    // List all class (GET)
    case Method.GET -> !! / "discipline" =>
      for {
        service <- ZIO.service[DisciplineService]
        list <- service.getDiscipline.tapError(e => ZIO.logError(e.getMessage))
      } yield Response.json(list.toJson)

    // Create a class (POST)
    case req @ Method.POST -> !! / "discipline" =>
      for {
        body    <- req.body.asString
        request <- ZIO.fromEither(body.fromJson[DisciplineRequest])
          .mapError(e => new Exception(s"Invalid JSON: $e"))
        service <- ZIO.service[DisciplineService]
        msg     <- service.postDiscipline(request.name,request.hours)
      } yield Response.text(msg)
  }
}
