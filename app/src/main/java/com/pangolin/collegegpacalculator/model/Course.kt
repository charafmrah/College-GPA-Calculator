package com.pangolin.collegegpacalculator.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val courseName: String,
    @ColumnInfo(name = "credit")
    val courseCredit: Int,
    @ColumnInfo(name = "grade")
    val courseGrade: String
)