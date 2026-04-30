package services.classe
import zio._
import models.classe.Class
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import scala.jdk.CollectionConverters._

trait ClassService {
  def postClass(name: String, number: String): Task[String]
  def getClasses: Task[List[Class]]
}

case class ClassServiceImpl(g: GraphTraversalSource) extends ClassService {


  override def postClass(name: String, number: String): Task[String] =
    ZIO.attemptBlocking {
      g.addV("class")
        .property("name", name)
        .property("number", number)
        .next()
    }
      .map(vertex => vertex.id().toString)
      .tapError(e => ZIO.logError(s"POST Error: ${e.getMessage}"))

  override def getClasses: Task[List[Class]] =
    ZIO.attemptBlocking {
      val nodes = g.V().hasLabel("class").valueMap(true).toList().asScala
      nodes.map { rawMap =>
        val map = rawMap.asInstanceOf[java.util.Map[AnyRef, AnyRef]]

        val id    = rawMap.get(org.apache.tinkerpop.gremlin.structure.T.id).toString
        val label = rawMap.get(org.apache.tinkerpop.gremlin.structure.T.label).toString

        val name = Option(map.get("name"))
          .map(_.asInstanceOf[java.util.List[String]].get(0))
          .getOrElse("")

        val number = Option(map.get("number"))
          .map(_.asInstanceOf[java.util.List[String]].get(0))
          .getOrElse("")

        Class(id, label, name, number)
      }.toList
    }.tapError(e => ZIO.logError(s"GET Error: ${e.getMessage}"))
}

object ClassService {
  val live: ZLayer[GraphTraversalSource, Nothing, ClassService] =
    ZLayer.fromFunction(ClassServiceImpl(_))

}
