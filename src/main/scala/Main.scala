import scala.util.matching.Regex

object Main {

	def main(args: Array[String]): Unit = {
		var programEnd: Boolean = false
		val course: Course = new Course()
		val isDigit: Regex = "^[0-9]*$".r
		while (!programEnd) {
			val input: String = io.console()
			input match {
				case "q" => programEnd = true
				case "s" => course.syncAll()
				case "l" => course.listCourse()
				case "t" => course.test()
				case "h" | "help" => io.printHelp()
				case isDigit() => course.queryCourse(input.toInt)
				case _ => ()
			}
		}
	}

}