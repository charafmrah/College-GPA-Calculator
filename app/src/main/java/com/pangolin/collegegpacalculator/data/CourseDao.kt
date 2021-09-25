package com.pangolin.collegegpacalculator.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(course: Course)

    @Update
    suspend fun update(course: Course)

    @Delete
    suspend fun delete(course: Course)

    @Query("SELECT * FROM course WHERE id = :id")
    fun getCourse(id: Int): Flow<Course>

    @Query("SELECT * FROM course ORDER BY name ASC")
    fun getCourses(): Flow<List<Course>>
}