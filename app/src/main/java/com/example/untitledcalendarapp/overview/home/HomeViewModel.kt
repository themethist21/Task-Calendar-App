package com.example.untitledcalendarapp.overview.home

import android.content.ClipData
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.applandeo.materialcalendarview.EventDay
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.data.Task
import com.example.untitledcalendarapp.data.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeViewModel(private val taskDao: TaskDao): ViewModel() {
    //Get items from database
    val allTasks:LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    private var _allItems: LiveData<List<Task>> = taskDao.getTasks().asLiveData()
    val allItems:LiveData<List<Task>>
        get() = _allItems

    private val dateSdf = SimpleDateFormat("MMM dd",Locale.getDefault())
    private fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    val allDays: LiveData<List<String>> = taskDao.getDistinctDays().asLiveData()

    private var _tasksByDay: MutableList<LiveData<List<Task>>> = mutableListOf()
    val tasksByDay:List<LiveData<List<Task>>>
        get() = _tasksByDay


    private fun getNewTaskEntry(taskTitle: String, taskDescription:String, taskCalendar: Calendar,taskPrio:Int): Task {
        return Task(
            taskDescription = taskDescription.toString(),
            taskTitle = taskTitle.toString(),
            taskDate = taskCalendar,
            taskMilliseconds = taskCalendar.timeInMillis,
            taskDay = dateSdf.format(taskCalendar.time),
            taskHour = taskCalendar.get(Calendar.HOUR_OF_DAY).toString(),
            taskPrio = taskPrio
        )

    }
    private fun getUpdatedTaskEntry(
        taskId: Int,
        taskTitle: String,
        taskDescription: String,
        taskCalendar: Calendar,
        taskPrio: Int
    ): Task {
        return Task(
            id = taskId,
            taskDescription = taskDescription.toString(),
            taskTitle = taskTitle.toString(),
            taskDate = taskCalendar,
            taskMilliseconds = taskCalendar.timeInMillis,
            taskDay = dateSdf.format(taskCalendar.time),
            taskHour = taskCalendar.get(Calendar.HOUR_OF_DAY).toString(),
            taskPrio = taskPrio
        )
    }
    private fun updateTask(task:Task){
        viewModelScope.launch{
            taskDao.update(task)
        }
    }

    private fun getQueryTasks(query:String):LiveData<List<Task>>?{
        return taskDao.getTasksByQuery(query).asLiveData()
    }

    private fun changeAllItems(case: Int){
        when(case){
            1 -> _allItems = taskDao.getTasks().asLiveData()
            2 -> _allItems = taskDao.getTitleDesc().asLiveData()
            3 -> _allItems = taskDao.getEarliest().asLiveData()
            4 -> _allItems = taskDao.getLatest().asLiveData()
            5 -> _allItems = taskDao.getMostImportant().asLiveData()
            6 -> _allItems = taskDao.getLeastImportant().asLiveData()
        }
    }
    private fun getTasksByDay(){
        if(allDays.value != null){
            for (day in allDays.value!!){
                viewModelScope.launch{
                    _tasksByDay.add(taskDao.getDayTasks(day).asLiveData())
                }
            }
        }
    }


    fun addNewTask(taskTitle: String, taskDescription:String, taskCalendar: Calendar,taskPrio:Int) {
        val newTask = getNewTaskEntry(taskTitle, taskDescription, taskCalendar,taskPrio)
        insertTask(newTask)
    }

    fun isEntryValid(taskTitle: String, taskDescription:String): Boolean {
        if (taskTitle.isBlank() || taskDescription.isBlank()) {
            return false
        }
        return true
    }
    fun retrieveTask(id:Int):LiveData<Task>{
        return taskDao.getTask(id).asLiveData()
    }

    fun deleteTask(task:Task){
        viewModelScope.launch{
            taskDao.delete(task)
        }
    }
    fun updateTask(
        taskId: Int,
        taskTitle: String,
        taskDescription: String,
        taskCalendar: Calendar,
        taskPrio: Int
    ) {
        val updatedTask = getUpdatedTaskEntry(taskId,taskTitle,taskDescription,taskCalendar,taskPrio)
        updateTask(updatedTask)
    }
    fun getTasksByQuery(query:String):LiveData<List<Task>>?{
        return getQueryTasks(query)
    }
    fun getOrderedTasks(case:Int){
        changeAllItems(case)
    }
    fun getDailyTasks(){
        getTasksByDay()
    }
}
class HomeViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}