package com.example.untitledcalendarapp.overview

import android.app.Application
import com.example.untitledcalendarapp.data.TaskRoomDatabase


class CalendarApplication : Application(){
    val database: TaskRoomDatabase by lazy { TaskRoomDatabase.getDatabase(this) }
}