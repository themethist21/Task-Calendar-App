package com.example.untitledcalendarapp.overview.create

import android.widget.TimePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.untitledcalendarapp.R
import java.util.*

class CreateViewModel: ViewModel() {
    private var _calendar = Calendar.getInstance()
    val calendar:Calendar
        get() = _calendar

    private val _day = MutableLiveData<Int>(0)
    val day:LiveData<Int>
        get() = _day
    private val _hourOfDay = MutableLiveData<Int>(0)
    val hourOfDay:LiveData<Int>
        get() = _hourOfDay


    fun setDate (year: Int, month: Int, day: Int){
        _calendar.set(Calendar.YEAR,year)
        _calendar.set(Calendar.MONTH,month)
        _calendar.set(Calendar.DAY_OF_MONTH,day)
        _day.value = day
    }

    fun setTime(hourOfDay: Int, minute: Int){
        _calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
        _calendar.set(Calendar.MINUTE,minute)
        _hourOfDay.value = hourOfDay
    }
    fun resetCalendar(){
        _calendar = Calendar.getInstance()
    }
    fun overrideCalendar(newCalendar : Calendar?){
        _calendar = newCalendar
        _day.value = newCalendar?.get(Calendar.DAY_OF_MONTH)
        _hourOfDay.value = newCalendar?.get(Calendar.HOUR_OF_DAY)
    }

}