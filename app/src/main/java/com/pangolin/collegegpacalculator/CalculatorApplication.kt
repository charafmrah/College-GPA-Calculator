package com.pangolin.collegegpacalculator

import android.app.Application
import com.pangolin.collegegpacalculator.data.CourseRoomDatabase

class CalculatorApplication : Application() {
    val database: CourseRoomDatabase by lazy { CourseRoomDatabase.getDatabase(this) }
}