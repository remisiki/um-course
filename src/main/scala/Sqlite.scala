import java.sql.{DriverManager,SQLException}

object Sqlite {

	def execute(requests: Array[String]): Boolean = {
		var response = false
		Class.forName("org.sqlite.JDBC")
		val connection = DriverManager.getConnection("jdbc:sqlite:data/data.db")
		val statement = connection.createStatement()
		try {
			connection.setAutoCommit(false)
			for (request <- requests) {
				statement.addBatch(request)
			}
			try {
				statement.executeBatch()
				connection.commit()
				response = true
			} catch {
				case _: SQLException => response = false
			}
		} catch {
			case _: SQLException => response = false
		} finally {
			try {
				statement.close()
			} catch {
				case _: SQLException => response = false
			}
			try {
				connection.close()
			} catch {
				case _: SQLException => response = false
			}
		}
		response
	}

	def query(request: String): Array[Array[String]] = {
		var response = false
		Class.forName("org.sqlite.JDBC")
		val connection = DriverManager.getConnection("jdbc:sqlite:data/data.db")
		val statement = connection.createStatement()
		var resultSet: Array[Array[String]] = Array.empty
		try {
			val rs = statement.executeQuery(request)
			val columnSize = rs.getMetaData().getColumnCount()
			while (rs.next()) {
				val resultRow: Array[String] = {
					var result: Array[String] = Array.empty
					for (i <- 1 to columnSize) {
						result = result :+ rs.getString(i)
					}
					result
				}
				resultSet = resultSet :+ resultRow
			}
		} catch {
			case e: SQLException => ()
		} finally {
			try {
				statement.close()
			} catch {
				case e: SQLException => ()
			}
			try {
				connection.close()
			} catch {
				case e: SQLException => ()
			}
		}
		resultSet
	}

	def init(): Boolean = {
		io.createDataPath()
		val requests = Array(
			"""
				CREATE TABLE IF NOT EXISTS UMSI_COURSE_LIST(
					id INTEGER UNIQUE,
					name TEXT,
					desc TEXT,
					preq_adv TEXT,
					preq_frc TEXT
				)
			"""
		)
		this.execute(requests)
	}

	def storeCourse(id: Int, name: String, desciption: String, advisePrerequisite: String, forcePrerequisite: String): Boolean = {
		val requests = Array(
			s"""
				INSERT OR REPLACE INTO UMSI_COURSE_LIST VALUES(
					${id},
					"${name.replace("\"", "\"\"")}",
					"${desciption.replace("\"", "\"\"")}",
					"${advisePrerequisite.replace("\"", "\"\"")}",
					"${forcePrerequisite.replace("\"", "\"\"")}"
				)
			"""
		)
		this.execute(requests)
	}

	def queryCourse(id: Int): Unit = {
		val request = s"SELECT * FROM UMSI_COURSE_LIST WHERE id = ${id}"
		val resultSet = this.query(request)
		io.printCourse(resultSet)
	}

	def listCourse(): Unit = {
		val request = s"SELECT name FROM UMSI_COURSE_LIST"
		val resultSet = this.query(request)
		io.printCourseList(resultSet)
	}

}