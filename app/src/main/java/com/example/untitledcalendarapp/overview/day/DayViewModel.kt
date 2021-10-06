package com.example.untitledcalendarapp.overview.day

import androidx.lifecycle.*
import com.example.untitledcalendarapp.data.Task
import com.example.untitledcalendarapp.data.TaskDao
import com.example.untitledcalendarapp.overview.home.HomeViewModel
import kotlinx.coroutines.launch

class DayViewModel(private val taskDao: TaskDao): ViewModel() {
    private lateinit var _allDayItems: LiveData<List<Task>>
    val allDayItems:LiveData<List<Task>>
        get() = _allDayItems

    private var _tasksByHour: MutableList<LiveData<List<Task>>> = mutableListOf()
    val tasksByHour:List<LiveData<List<Task>>>
        get() = _tasksByHour

    private fun getDayTasks(day:String){
        viewModelScope.launch{
            _allDayItems = taskDao.getDayTasksByPrio(day).asLiveData()
        }
        for (i in 0..24){
            viewModelScope.launch{
                _tasksByHour.add(taskDao.getDayTasksByHour(day,i.toString()).asLiveData())
            }
        }
    }

    fun setAllTasks(day:String){
        getDayTasks(day)
    }
}

class DayViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DayViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}