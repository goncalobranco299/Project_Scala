import zio._
import zio.http._
import org.apache.tinkerpop.gremlin.driver.{Client, Cluster}
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import routes.{StudentRoutes, ClassRoutes, DisciplineRoutes, CourseRoutes}
import services.student.StudentService
import services.classe.ClassService
import services.discipline.DisciplineService
import services.course.CourseService

object MainApp extends ZIOAppDefault {

  // 1. Camada para o Cluster do Gremlin (Garante o fechamento correto)
  val clusterLayer: ZLayer[Any, Throwable, Cluster] = ZLayer.scoped {
    ZIO.acquireRelease(
      ZIO.attempt(Cluster.build().addContactPoint("localhost").port(8182).create())
    )(cluster => ZIO.attempt(cluster.close()).ignore)
  }

  // 2. Camada para a Traversal (g) dependente do Cluster
  val gremlinLayer: ZLayer[Cluster, Throwable, GraphTraversalSource] = ZLayer.scoped {
    for {
      cluster <- ZIO.service[Cluster]
      g       <- ZIO.attemptBlocking(traversal().withRemote(DriverRemoteConnection.using(cluster, "g")))
    } yield g
  }

  // 3. Composição de todas as rotas
  val routes = (StudentRoutes.app ++ ClassRoutes.app ++ DisciplineRoutes.app ++ CourseRoutes.app).withDefaultErrorResponse

  override def run = {
    Server.serve(routes)
      .provide(
        // Configurações do Servidor HTTP
        ServerConfig.live(ServerConfig.default.port(8080)),
        Server.live,

        // Infraestrutura do Grafo
        clusterLayer,
        gremlinLayer,

        // Seus Serviços Business
        StudentService.live,
        ClassService.live,
        DisciplineService.live,
        CourseService.live
      )
  }
}