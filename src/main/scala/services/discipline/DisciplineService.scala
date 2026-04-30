package services.discipline
import models.GraphLabes
import zio._
import models.discipline.{Discipline, DisciplineRequest}
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex

import scala.jdk.CollectionConverters._

trait DisciplineService {
  def postDiscipline(discipline: DisciplineRequest, teacherId: String): Task[String]
  def getDisciplines: Task[List[Discipline]]
}

case class DisciplineServiceImpl(g: GraphTraversalSource) extends DisciplineService {

  override def postDiscipline(req: DisciplineRequest, teacherId: String): Task[String] =
    ZIO.attemptBlocking {
        g.V(teacherId).as("prof")
          .addV(GraphLabes.disciplineVertex)
          .property("name", req.name)
          .property("hours", Integer.valueOf(req.hours))
          .as("d")
          .addE(GraphLabes.teachEdge).from("prof").to("d")
          .select[Vertex]("d")
          .next()
      }.map(_.id().toString)
      .tapError(e => ZIO.logError(s"POST Error: ${e.getMessage}"))

  override def getDisciplines: Task[List[Discipline]] =
    ZIO.attemptBlocking {
      val nodes = g.V().hasLabel(GraphLabes.disciplineVertex).valueMap(true).toList().asScala
      nodes.map { map =>
        val id    = map.get(org.apache.tinkerpop.gremlin.structure.T.id).toString
        val label = map.get(org.apache.tinkerpop.gremlin.structure.T.label).toString
        val name  = map.get("name").asInstanceOf[java.util.List[String]].get(0)
        val hours = map.get("hours").asInstanceOf[java.util.List[Int]].get(0)
        Discipline(id, label, name, hours)
      }.toList
    }.tapError(e => ZIO.logError(s"GET Error: ${e.getMessage}"))
}

object DisciplineService {
  val live: ZLayer[GraphTraversalSource, Nothing, DisciplineService] =
    ZLayer.fromFunction(DisciplineServiceImpl(_))
}