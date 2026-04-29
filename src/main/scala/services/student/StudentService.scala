package services.student
import zio._
import models.student.Student
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import scala.jdk.CollectionConverters._


trait StudentService {
  def postStudent(name: String, age: Int): Task[String]
  def getStudents: Task[List[Student]]
}

// Service Implementation
case class StudentServiceImpl(g: GraphTraversalSource) extends StudentService {

  override def postStudent(name: String, age: Int): Task[String] =
    ZIO.attemptBlocking {
        g.addV("student")
          .property("name", name)
          .property("age", Integer.valueOf(age))
          .next()
      }
      .map(vertex => vertex.id().toString)
      .tapError(e => ZIO.logError(s"POST Error: ${e.getMessage}"))

  override def getStudents: Task[List[Student]] =
    ZIO.attemptBlocking {
      val nodes = g.V().hasLabel("student").valueMap(true).toList().asScala
      nodes.map { map =>
        val id    = map.get(org.apache.tinkerpop.gremlin.structure.T.id).toString
        val label = map.get(org.apache.tinkerpop.gremlin.structure.T.label).toString

        val name  = map.get("name").asInstanceOf[java.util.List[String]].get(0)
        val age   = map.get("age").asInstanceOf[java.util.List[Int]].get(0)
        Student(id, label, name, age)
      }.toList
    }.tapError(e => ZIO.logError(s"GET Error: ${e.getMessage}"))
}

// Dependency Injection Layer
object StudentService {
  val live: ZLayer[GraphTraversalSource, Nothing, StudentService] =
    ZLayer.fromFunction(StudentServiceImpl(_))
}
