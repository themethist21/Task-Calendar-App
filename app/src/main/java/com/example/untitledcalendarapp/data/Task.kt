package com.example.untitledcalendarapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo(name = "title")
    val taskTitle: String,
    @ColumnInfo(name = "description")
    val taskDescription: String,
    @ColumnInfo(name = "date")
    val taskDate: Calendar?,
    @ColumnInfo(name = "milliseconds")
    val taskMilliseconds:Long,
    @ColumnInfo(name = "day")
    val taskDay:String,
    @ColumnInfo(name = "hour")
    val taskHour:String,
    @ColumnInfo(name = "priority")
    val taskPrio: Int
)
