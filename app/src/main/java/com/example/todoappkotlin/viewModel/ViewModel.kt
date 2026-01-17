package com.example.todoappkotlin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappkotlin.model.TodoModel
import com.example.todoappkotlin.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    val todoList: StateFlow<List<TodoModel>> = repository.todoList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTodo(title: String, desc: String) {
        viewModelScope.launch {
            repository.addTodo(title, desc)
        }
    }

    fun deleteTodo(todo: TodoModel) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    fun toggleComplete(todo: TodoModel) {
        viewModelScope.launch {
            repository.toggleComplete(todo)
        }
    }

    fun updateTodo(todo: TodoModel, title: String, desc: String) {
        viewModelScope.launch {
            repository.updateTodo(todo, title, desc)
        }
    }

    fun clearAllTodos() {
        viewModelScope.launch {
            repository.clearAllTodos()
        }
    }
}
