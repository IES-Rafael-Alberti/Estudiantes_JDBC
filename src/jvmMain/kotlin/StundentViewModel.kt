import androidx.compose.runtime.*
import java.io.File

class StudentsViewModel(
    private val studentsFile: File
) {
    private var _newStudent by mutableStateOf("")
    val newStudent: String
        get() = _newStudent

    var students by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        students = readStudentsFromFile(studentsFile)
    }

    fun addStudent() {
        if (_newStudent.isNotBlank()) {
            students = students + _newStudent
            _newStudent = ""
        }
    }

    fun clearStudents() {
        students = emptyList()
    }

    fun saveChanges() {
        try {
            studentsFile.writeText(students.joinToString("\n"))
            println("Changes saved successfully.")
        } catch (e: Exception) {
            println("Error while saving changes: ${e.message}")
        }
    }

    fun newStudentChange(name: String) {
        _newStudent = name
    }

    fun removeStudent(index: Int) {
        students = students.toMutableList().apply { removeAt(index) }
    }

    private fun readStudentsFromFile(file: File): List<String> {
        val students = mutableListOf<String>()
        try {
            val reader = file.bufferedReader()
            reader.useLines { lines ->
                lines.forEach { line ->
                    students.add(line)
                }
            }
            println("Students content read from the file: $students")
        } catch (e: Exception) {
            println("Error while reading the file: ${e.message}")
        }
        return students
    }
}