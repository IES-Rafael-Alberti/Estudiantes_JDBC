import androidx.compose.runtime.*

class StudentsViewModelDb(private val studentRepository: StudentRepository) {
    private var _newStudent = mutableStateOf("")
    val newStudent: State<String> = _newStudent

    private var _students = mutableStateOf<List<String>>(emptyList())
    val students: State<List<String>> = _students

    init {
        loadStudents()
    }

    fun addStudent() {
        if (_newStudent.value.isNotBlank()) {
            _students.value += _newStudent.value.trim()
            _newStudent.value = ""
        }
    }

    fun clearStudents() {
        _students.value = emptyList()
    }

    fun saveChanges() {
        studentRepository.updateStudents(_students.value)
    }

    fun newStudentChange(name: String) {
        _newStudent.value = name
    }

    private fun loadStudents() {
        val result = studentRepository.getAllStudents()
        result.onSuccess { students ->
            _students.value = students
        }
        result.onFailure { e ->
            println("Error loading students: ${e.message}")
        }
    }
}
