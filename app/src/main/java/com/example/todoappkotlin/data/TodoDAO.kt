package com.example.todoappkotlin.data

import androidx.room.*
import com.example.todoappkotlin.model.TodoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoModel)

    @Update
    suspend fun updateTodo(todo: TodoModel)

    @Delete
    suspend fun deleteTodo(todo: TodoModel)

    @Query("DELETE FROM todo_table")
    suspend fun clearAllTodos()
}