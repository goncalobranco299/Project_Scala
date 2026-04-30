package services.student
import zio._
import models.GraphLabes
import models.student.{Student, StudentRequest}
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex

import scala.jdk.CollectionConverters._


trait StudentService {
  def postStudent(req: StudentRequest, classId: String): Task[String]
  def getStudents: Task[List[Student]]
}

// Service Implementation
case class StudentServiceImpl(g: GraphTraversalSource) extends StudentService {

  override def postStudent(req: StudentRequest, classId: String): Task[String] =
    ZIO.attemptBlocking {
        g.V().has(GraphLabes.classeVertex,"id", classId).as("c")
          .addV(GraphLabes.studentVertex)
          .property("name", req.name)
          .property("email",req.email)
          .property("studentNumber", req.studentNumber)
          .property("classId", classId)
          .as("s")
          .addE(GraphLabes.enrolledInEdge).from("s").to("c")
          .select[Vertex]("s")
          .next()
      }.map(_.id().toString)
      .tapError(e => ZIO.logError(s"Erro ao criar Aluno: ${e.getMessage}"))

  override def getStudents: Task[List[Student]] =
    ZIO.attemptBlocking {
      val nodes = g.V().hasLabel(GraphLabes.studentVertex).valueMap(true).toList().asScala
      nodes.map { map =>
        val id    = map.get(org.apache.tinkerpop.gremlin.structure.T.id).toString
        val label = map.get(org.apache.tinkerpop.gremlin.structure.T.label).toString
        val name  = map.get("name").asInstanceOf[java.util.List[String]].get(0)
        val email  = map.get("email").asInstanceOf[java.util.List[String]].get(0)
        val num  = map.get("studentNumber").asInstanceOf[java.util.List[String]].get(0)
        val classId =
          if (map.get("classId") != null)
            map.get("classId").asInstanceOf[java.util.List[String]].get(0)
          else
            ""
        Student(id, label, name, email, num, classId)
      }.toList
    }.tapError(e => ZIO.logError(s"GET Error: ${e.getMessage}"))
}

// Dependency Injection Layer
object StudentService {
  val live: ZLayer[GraphTraversalSource, Nothing, StudentService] =
    ZLayer.fromFunction(StudentServiceImpl(_))
}
