/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pangolin.collegegpacalculator.viewmodels

import androidx.lifecycle.*
import com.pangolin.collegegpacalculator.model.Course
import com.pangolin.collegegpacalculator.model.CourseDao
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

/**
 * View Model to keep a reference to the GPA Calculator repository and an up-to-date list of all courses.
 *
 */
class CalculatorViewModel(private val courseDao: CourseDao) : ViewModel() {

    // Cache all courses from the database using LiveData
    val allCourses: LiveData<List<Course>> = courseDao.getCourses().asLiveData()

    fun calculateGpa(): Double {
        val gpa = 0.0
        var creditTimesGrade = 0.0
        var credits = 0

        allCourses.value?.forEach{
            credits += it.courseCredit
        }

        allCourses.value?.forEach {
            creditTimesGrade += it.courseCredit * letterGradeToNumber(it.courseGrade)
        }

        return String.format("%.2f", creditTimesGrade / credits).toDouble()
    }

    fun letterGradeToNumber(letterGrade: String): Double {
        var result: Double

        when(letterGrade) {
            "A" -> result = 4.0
            "A-" -> result = 3.67
            "B+" -> result = 3.33
            "B" -> result = 3.0
            "B-" -> result = 2.67
            "C+" -> result = 2.33
            "C" -> result = 2.0
            "C-" -> result = 1.67
            "D+" -> result = 1.33
            "D" -> result = 1.0
            "D-" -> result = 0.67
            else -> result = 0.0
        }

        return result
    }

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