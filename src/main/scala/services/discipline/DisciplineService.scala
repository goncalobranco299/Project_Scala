package services.discipline
import zio._
import models.discipline.Discipline
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import scala.jdk.CollectionConverters._


trait DisciplineService {
  def postDiscipline(name: String, hours: Int): Task[String]
  def getDiscipline: Task[List[Discipline]]
}

// Service Implementation
case class DisciplineServiceImpl(g: GraphTraversalSource) extends DisciplineService {

  override def postDiscipline(name: String, hours: Int): Task[String] =
    ZIO.attemptBlocking {
        g.addV("discipline")
          .property("name", name)
          .property("hours", Integer.valueOf(hours))
          .next()
      }
      .map(vertex => vertex.id().toString)
      .tapError(e => ZIO.logError(s"POST Error: ${e.getMessage}"))

  override def getDiscipline: Task[List[Discipline]] =
    ZIO.attemptBlocking {
      val nodes = g.V().hasLabel("discipline").valueMap(true).toList().asScala
      nodes.map { map =>
        val id    = map.get(org.apache.tinkerpop.gremlin.structure.T.id).toString
        val label = map.get(org.apache.tinkerpop.gremlin.structure.T.label).toString

        val name  = map.get("name").asInstanceOf[java.util.List[String]].get(0)
        val hours   = map.get("hours").asInstanceOf[java.util.List[Int]].get(0)
        Discipline(id, label, name, hours)
      }.toList
    }.tapError(e => ZIO.logError(s"GET Error: ${e.getMessage}"))
}

// Dependency Injection Layer
object DisciplineService {
  val live: ZLayer[GraphTraversalSource, Nothing, DisciplineService] =
    ZLayer.fromFunction(DisciplineServiceImpl(_))
}
