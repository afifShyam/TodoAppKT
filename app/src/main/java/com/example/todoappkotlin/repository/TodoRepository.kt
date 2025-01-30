package com.example.todoappkotlin.repository

import com.example.todoappkotlin.model.TodoModel
import com.example.todoappkotlin.data.TodoDao
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    val todoList: Flow<List<TodoModel>> = todoDao.getAllTodos()

    suspend fun addTodo(title: String, desc: String) {
        val todo = TodoModel(
            title = title,
            description = desc,
            isDone = false,
            createdAt = Date()
        )
        todoDao.insertTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoModel) {
        todoDao.deleteTodo(todo)
    }

    suspend fun toggleComplete(todo: TodoModel) {
        todoDao.updateTodo(todo.copy(isDone = !todo.isDone))
    }

    suspend fun clearAllTodos() {
        todoDao.clearAllTodos()
    }
}