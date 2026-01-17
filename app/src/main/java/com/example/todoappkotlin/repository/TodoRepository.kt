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
        val cleanTitle = title.trim()
        val cleanDesc = desc.trim()
        if (cleanTitle.isBlank() || cleanDesc.isBlank()) {
            return
        }
        val now = Date()
        val todo = TodoModel(
            title = cleanTitle,
            description = cleanDesc,
            isDone = false,
            createdAt = now,
            updatedAt = now
        )
        todoDao.insertTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoModel) {
        todoDao.deleteTodo(todo)
    }

    suspend fun toggleComplete(todo: TodoModel) {
        todoDao.updateTodo(
            todo.copy(
                isDone = !todo.isDone,
                updatedAt = Date()
            )
        )
    }

    suspend fun updateTodo(todo: TodoModel, title: String, desc: String) {
        val cleanTitle = title.trim()
        val cleanDesc = desc.trim()
        if (cleanTitle.isBlank() || cleanDesc.isBlank()) {
            return
        }
        todoDao.updateTodo(
            todo.copy(
                title = cleanTitle,
                description = cleanDesc,
                updatedAt = Date()
            )
        )
    }

    suspend fun clearAllTodos() {
        todoDao.clearAllTodos()
    }
}
