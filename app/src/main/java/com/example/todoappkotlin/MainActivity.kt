package com.example.todoappkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoappkotlin.component.AddFloatingBtn
import com.example.todoappkotlin.component.BottomSheetDemo
import com.example.todoappkotlin.component.TodoAppBar
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
                val coroutineScope = rememberCoroutineScope()

                // ✅ Define sheetState at the top-level
                val sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                var isSheetOpen by remember { mutableStateOf(false) } // ✅ Control visibility

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TodoAppBar() },
                    floatingActionButton = {
                        AddFloatingBtn(
                            onClick = {
                                coroutineScope.launch {
                                    isSheetOpen = true  // ✅ Set visibility state
                                    sheetState.show()    // ✅ Trigger bottom sheet opening
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    TodoListPage(
                        viewModel = todoViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )

                    // ✅ Show Bottom Sheet when isSheetOpen = true
                    if (isSheetOpen) {
                        BottomSheetDemo(
                            sheetState = sheetState,
                            onDismiss = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    isSheetOpen = false  // ✅ Close sheet properly
                                }
                            },
                            onAddTask = { title, description ->
                                todoViewModel.addTodo(title, description)
                                coroutineScope.launch {
                                    sheetState.hide()
                                    isSheetOpen = false  // ✅ Hide after adding task
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}