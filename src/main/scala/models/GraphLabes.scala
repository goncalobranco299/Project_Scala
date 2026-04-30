package models

object GraphLabes {

  // Vertex
  val studentVertex = "Student"
  val teacherVertex = "Teacher"
  val classeVertex = "Classe"
  val disciplineVertex = "Discipline"
  val courseVertex = "Course"


  // Edges
  val enrolledInEdge = "ENROLLED_IN" // Student -> Classe
  val teachEdge = "TEACH" // Teacher -> Classe
  val belongsToEdge = "BELONGS_TO" // Classe -> Discipline
  val partOfEdge = "PART_OF" // Discipline -> Course
  val reisteredinEdge = "REGISTERED_IN" // Student -> Course
  val evaluatedInEdge = "EVALUATED_IN" // Student -> Discipline (com nota)
}

