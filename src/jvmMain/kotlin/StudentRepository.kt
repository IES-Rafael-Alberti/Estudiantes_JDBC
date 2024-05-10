import java.sql.*

object Database {
    private const val URL = "jdbc:mysql://localhost:3306/studentdb"
    private const val USER = "studentuser"
    private const val PASSWORD = "password"
    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
    fun getConnection(): Connection = DriverManager.getConnection(URL, USER, PASSWORD)
}

class StudentRepository {
    fun getAllStudents(): Result<List<String>> {
        return try {
            val connectionDb = Database.getConnection()
            val students = mutableListOf<String>()
            val stmt = connectionDb.createStatement()
            val rs = stmt.executeQuery("SELECT name FROM students")
            while (rs.next()) {
                students.add(rs.getString("name"))
            }
            rs.close()
            stmt.close()
            connectionDb.close()
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateStudents(students: List<String>): Result<Unit> {
        var connectionDb: Connection? = null
        return try {
            connectionDb = Database.getConnection()
            connectionDb.autoCommit = false
            val stmt = connectionDb.createStatement()
            stmt.execute("DELETE FROM students")
            val ps = connectionDb.prepareStatement("INSERT INTO students (name) VALUES (?)")
            for (student in students) {
                ps.setString(1, student)
                ps.executeUpdate()
            }
            ps.close()
            stmt.close()
            connectionDb.commit()
            connectionDb.close()
            Result.success(Unit)
        } catch (e: Exception) {
            connectionDb?.rollback()
            Result.failure(e)
        } finally {
            connectionDb?.autoCommit = true
            connectionDb?.close()
        }
    }
}
