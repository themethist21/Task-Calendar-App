package com.example.untitledcalendarapp.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class TaskConverters {
    @TypeConverter
    fun fromString(value: String?): Calendar? {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault())
        val cal = Calendar.getInstance()
        value?.let {cal.time = sdf.parse(value) }
        return cal
    }

    @TypeConverter
    fun calendarToString(calendar: Calendar?): String {
        return calendar?.time.toString()
    }
}