package services.course
import zio._
import models.course.Course
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import scala.jdk.CollectionConverters._

trait CourseService {
  def postCourse(name: String): Task[String]
  def getCourse: Task[List[Course]]
}

case class CourseServiceImpl(g: GraphTraversalSource) extends CourseService {

  override def postCourse(name: String): Task[String] =
    ZIO.attemptBlocking {
      g.addV("course")
        .property("name", name)
        .next()
    }
    .map(vertex => vertex.id().toString)
    .tapError(e => ZIO.logError(s"POST Error: ${e.getMessage}"))

  override def getCourse: Task[List[Course]] =
    ZIO.attemptBlocking {
      val nodes = g.V().hasLabel("course").valueMap(true).toList().asScala
      nodes.map { map =>
        val id    = map.get(org.apache.tinkerpop.gremlin.structure.T.id).toString
        val label = map.get(org.apache.tinkerpop.gremlin.structure.T.label).toString

        val name  = map.get("name").asInstanceOf[java.util.List[String]].get(0)
        Course(id, label, name)
      }.toList
    }.tapError(e => ZIO.logError(s"GET Error: ${e.getMessage}"))
}

  // Dependency Injection Layer
  object CourseService {
    val live: ZLayer[GraphTraversalSource, Nothing, CourseService] =
      ZLayer.fromFunction(CourseServiceImpl(_))
}