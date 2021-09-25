package com.pangolin.collegegpacalculator.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Course::class], version = 1, exportSchema = false)
abstract class CourseRoomDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: CourseRoomDatabase? = null

        fun getDatabase(context: Context): CourseRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CourseRoomDatabase::class.java,
                    "course_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}