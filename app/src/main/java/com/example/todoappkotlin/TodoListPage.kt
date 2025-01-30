package com.example.todoappkotlin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoappkotlin.component.TodoListItem
import com.example.todoappkotlin.viewModel.TodoViewModel

@Composable
fun TodoListPage(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val todos by viewModel.todoList.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        if (todos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No Todos Found! \nPlease Add One", Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(todos) { _, todo ->
                    TodoListItem(
                        item = todo,
                        onDelete = { viewModel.deleteTodo(todo) },
                        onToggleComplete = { viewModel.toggleComplete(todo) }
                    )
                }
            }
        }
    }
}