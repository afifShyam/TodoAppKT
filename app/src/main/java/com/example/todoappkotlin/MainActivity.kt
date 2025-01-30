package com.example.todoappkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoappkotlin.component.TodoAppBar
import com.example.todoappkotlin.viewModel.TodoViewModel
import com.example.todoappkotlin.ui.theme.ToDoAppKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ToDoAppKotlinTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TodoAppBar()
                    }
                ) { innerPadding ->
                    // ✅ Use Hilt to Inject ViewModel Directly
                    val todoViewModel: TodoViewModel = hiltViewModel()

                    TodoListPage(
                        viewModel = todoViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}