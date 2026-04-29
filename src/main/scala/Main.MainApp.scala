import zio._
import zio.http._
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import routes.{ClassRoutes, CourseRoutes, DisciplineRoutes, StudentRoutes}
import services.student.StudentService
import services.classe.ClassService
import services.discipline.DisciplineService
import services.course.CourseService



object MainApp extends ZIOAppDefault {

  val gremlinLayer = ZLayer.scoped {
    for {
      cluster <- ZIO.attempt(Cluster.build().addContactPoint("localhost").port(8182).create())
      _ <- ZIO.addFinalizer(ZIO.attempt(cluster.close()).ignore)
      g <- ZIO.attemptBlocking(traversal().withRemote(DriverRemoteConnection.using(cluster, "g")))
    } yield g
  }

  override def run = {
    val combinedApps = StudentRoutes.app ++ ClassRoutes.app ++ DisciplineRoutes.app ++ CourseRoutes.app

    Server.serve(combinedApps.withDefaultErrorResponse)
      .provide(
        ServerConfig.live(ServerConfig.default.port(8080)),
        Server.live,
        gremlinLayer,
        StudentService.live,
        ClassService.live,
        DisciplineService.live,
        CourseService.live
      )
  }
}