package com.example.todoappkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoappkotlin.component.AddFloatingBtn
import com.example.todoappkotlin.component.AddTodoBottomSheet
import com.example.todoappkotlin.component.TodoAppBar
import com.example.todoappkotlin.model.TodoModel
import com.example.todoappkotlin.viewModel.TodoViewModel
import com.example.todoappkotlin.ui.theme.ToDoAppKotlinTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ToDoAppKotlinTheme {
                val todoViewModel: TodoViewModel = hiltViewModel()
                val todos by todoViewModel.todoList.collectAsStateWithLifecycle()
                val completedCount = remember(todos) { todos.count { it.isDone } }
                val coroutineScope = rememberCoroutineScope()
                var editingTodo by remember { mutableStateOf<TodoModel?>(null) }

                val sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                var isSheetOpen by rememberSaveable { mutableStateOf(false) }

                val openSheet: (TodoModel?) -> Unit = { todo ->
                    editingTodo = todo
                    if (!isSheetOpen) {
                        isSheetOpen = true
                    }
                    coroutineScope.launch { sheetState.show() }
                }

                val closeSheet: () -> Unit = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        isSheetOpen = false
                        editingTodo = null
                    }
                }


                val backgroundBrush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )


                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundBrush)
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        containerColor = Color.Transparent,
                        contentWindowInsets = WindowInsets.safeDrawing,
                        topBar = {
                            TodoAppBar(
                                todoCount = todos.size,
                                completedCount = completedCount,
                                onClearAll = todoViewModel::clearAllTodos,
                                scrollBehavior = scrollBehavior
                            )
                        },
                        floatingActionButton = { AddFloatingBtn(onClick = { openSheet(null) }) }
                    ) { innerPadding ->
                        TodoListPage(
                            todos = todos,
                            modifier = Modifier.padding(innerPadding),
                            onDelete = todoViewModel::deleteTodo,
                            onToggleComplete = todoViewModel::toggleComplete,
                            onEdit = { todo -> openSheet(todo) },
                            onAddClick = { openSheet(null) }
                        )

                        if (isSheetOpen) {
                            val currentTodo = editingTodo
                            AddTodoBottomSheet(
                                sheetState = sheetState,
                                onDismiss = closeSheet,
                                initialTitle = currentTodo?.title.orEmpty(),
                                initialDescription = currentTodo?.description.orEmpty(),
                                isEditing = currentTodo != null,
                                onSaveTask = { title, description ->
                                    if (currentTodo == null) {
                                        todoViewModel.addTodo(title, description)
                                    } else {
                                        todoViewModel.updateTodo(currentTodo, title, description)
                                    }
                                    closeSheet()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
