package com.example.todoappkotlin.component

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoappkotlin.viewModel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppBar() {
    val viewModel: TodoViewModel = hiltViewModel()
    val todoList = viewModel.todoList.collectAsStateWithLifecycle().value
    var showDialog by remember { mutableStateOf(false) } // ✅ State for dialog visibility

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "To-Do App",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF4CAF50),
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            if (todoList.isNotEmpty()) {
                TextButton(onClick = { showDialog = true }) {
                    Text(text = "Clear All", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    )

    //Show dialog confirmation before delete
    DeleteConfirmationDialog(showDialog, viewModel::clearAllTodos, { showDialog = false })

}