package com.pangolin.collegegpacalculator.viewmodels

import androidx.lifecycle.*
import com.pangolin.collegegpacalculator.model.Course
import com.pangolin.collegegpacalculator.model.CourseDao
import kotlinx.coroutines.launch

class CalculatorViewModel(private val courseDao: CourseDao) : ViewModel() {

    val allCourses: LiveData<List<Course>> = courseDao.getCourses().asLiveData()

    fun updateCourse(
        courseId: Int,
        courseName: String,
        courseCredit: String,
        courseGrade: String
    ) {
        val updatedCourse = getUpdatedCourseEntry(courseId, courseName, courseCredit, courseGrade)
        updateCourse(updatedCourse)
    }

    private fun updateCourse(course: Course) {
        viewModelScope.launch {
            courseDao.update(course)
        }
    }

    private fun getUpdatedCourseEntry(
        courseId: Int,
        courseName: String,
        courseCredit: String,
        courseGrade: String
    ): Course {
        return Course(
            id = courseId,
            courseName = courseName,
            courseCredit = courseCredit.toInt(),
            courseGrade = courseGrade
        )
    }

    fun addNewCourse(courseName: String, courseCredit: String, courseGrade: String) {
        val newCourse = getNewCourseEntry(courseName, courseCredit, courseGrade)
        insertCourse(newCourse)
    }

    private fun getNewCourseEntry(
        courseName: String, courseCredit: String, courseGrade: String) : Course {
        return Course(
            courseName = courseName,
            courseCredit = courseCredit.toInt(),
            courseGrade = courseGrade
        )
    }

    private fun insertCourse(course: Course) {
        viewModelScope.launch {
            courseDao.insert(course)
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            courseDao.delete(course)
        }
    }

    fun retrieveCourse(id: Int): LiveData<Course> {
        return courseDao.getCourse(id).asLiveData()
    }

    fun isEntryValid(
        courseName: String, courseCredit: String, courseGrade: String): Boolean {
        return !(courseName.isBlank() || courseCredit.isBlank() || courseGrade.isBlank())
    }

}

class CalculatorViewModelFactory(private val courseDao: CourseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalculatorViewModel(courseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}