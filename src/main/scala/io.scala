import scala.io.{StdIn,AnsiColor}
import java.nio.file.{Paths, Files}

object io {

	def console(): String = {
		print(s"${AnsiColor.GREEN}$$ ${AnsiColor.RESET}")
		StdIn.readLine()
	}

	def createDataPath(): Unit = {
		Files.createDirectories(Paths.get("data"))
	}

	def printCourse(resultSet: Array[Array[String]]): Unit = {
		val columns = Array(
			s"${AnsiColor.CYAN}SI ",
			s"${AnsiColor.YELLOW}Description ${AnsiColor.RESET}",
			s"${AnsiColor.YELLOW}Advisory Prerequisites ${AnsiColor.RESET}",
			s"${AnsiColor.YELLOW}Enforced Prerequisites ${AnsiColor.RESET}"
		)
		for (row <- resultSet) {
			println(s"${columns(0)}${row(1)}")
			println(s"${columns(1)}${row(2)}")
			if (row(3) != "") {
				println(s"${columns(2)}${row(3)}")
			}
			if (row(4) != "") {
				println(s"${columns(3)}${row(4)}")
			}
		}
	}

	def printCourseList(resultSet: Array[Array[String]]): Unit = {
		for (row <- resultSet) {
			println(row(0))
		}
	}

	def printHelp(): Unit = {
		println("""
			|This is a simple and stupid tool to grab course information from https://www.si.umich.edu/programs/courses. Not recommended to use.
			|Command List:
			|h: Show this help message.
			|q: Quit.
			|s: Sync all courses (brute force, very slow).
			|l: List all courses.
			|<course code>: Show information of course code.
			""".stripMargin)
	}

}