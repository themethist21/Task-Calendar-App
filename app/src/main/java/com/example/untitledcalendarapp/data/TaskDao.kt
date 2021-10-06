package com.example.untitledcalendarapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {
    @Query("SELECT * from task ORDER BY title ASC")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * from task WHERE id = :id")
    fun getTask(id: Int): Flow<Task>

    @Query("SELECT * from task WHERE day = :day")
    fun getDayTasks(day: String):Flow<List<Task>>

    @Query("SELECT * from task WHERE day = :day ORDER BY priority DESC")
    fun getDayTasksByPrio(day:String): Flow<List<Task>>

    @Query("SELECT * from task WHERE day = :day AND hour = :hour ORDER BY milliseconds ASC")
    fun getDayTasksByHour(day:String, hour:String): Flow<List<Task>>

    @Query("SELECT * from task WHERE title like '%'|| :query ||'%' OR date like '%'|| :query ||'%'")
    fun getTasksByQuery(query: String): Flow<List<Task>>

    @Query("SELECT * from task ORDER BY title DESC")
    fun getTitleDesc(): Flow<List<Task>>

    @Query("SELECT * from task ORDER BY milliseconds ASC")
    fun getEarliest(): Flow<List<Task>>

    @Query("SELECT * from task ORDER BY milliseconds DESC")
    fun getLatest(): Flow<List<Task>>

    @Query("SELECT * from task ORDER BY priority DESC")
    fun getMostImportant(): Flow<List<Task>>

    @Query("SELECT * from task ORDER BY priority ASC")
    fun getLeastImportant(): Flow<List<Task>>

    @Query("SELECT DISTINCT day from task")
    fun getDistinctDays(): Flow<List<String>>
    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}