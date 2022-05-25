import org.jsoup.{Jsoup,HttpStatusException}

class Course {

	private val baseURL: String = "https://www.si.umich.edu/programs/courses/"

	def syncCourse(courseId: Int): Unit = {
		try {
			val url = s"${this.baseURL}${courseId}"
			val doc = Jsoup.connect(url).get()
			val name = doc.select("#main-content h1 span").text()
			val desciption = doc.select("#main-content article").text()
			val advisePrerequisite = doc.select(".prerequisites-advisory li").text()
			val forcePrerequisite = doc.select(".prerequisites-enforced li").text()
			val storeSuccess = Sqlite.storeCourse(courseId, name, desciption, advisePrerequisite, forcePrerequisite)
			if (!storeSuccess) {
				throw CourseStoreFailException(courseId)
			}
		} catch {
			case e: HttpStatusException => throw CourseNotFoundException(courseId)
			case e: CourseStoreFailException => throw e
		}
	}

	def syncAll(): Unit = {
		val size = 200
		var syncSuccess = 0
		print("Fetching data: 0.0%")
		for (i <- 0 until size) {
			val courseId = 501 + i
			try {
				// if (courseId > 633) {
					this.syncCourse(courseId)
					syncSuccess += 1
				// }
			} catch {
				case e: CourseNotFoundException => ()
				case e: CourseStoreFailException => e.printStackTrace()
			} finally {
				print(f"\rFetching data: ${(i + 1).toFloat / size.toFloat * 100}%.1f%%")
			}
		}
		println()
		println(s"Updated ${syncSuccess} courses.")
	}

	def test(): Unit = {
		// println(Sqlite.storeCourse(501, "1", "1", "1", "1"))
		// this.syncCourse(520)
		println("This is a test.")
		// println(Sqlite.init())
	}

	def queryCourse(courseId: Int): Unit = {
		Sqlite.init()
		Sqlite.queryCourse(courseId)
	}

	def listCourse(): Unit = {
		Sqlite.init()
		Sqlite.listCourse()
	}

}

final case class CourseNotFoundException(courseId: Int) extends Exception(s"Course id ${courseId} not found.") {}

final case class CourseStoreFailException(courseId: Int) extends Exception(s"SQL failure when storing course id ${courseId}.") {}