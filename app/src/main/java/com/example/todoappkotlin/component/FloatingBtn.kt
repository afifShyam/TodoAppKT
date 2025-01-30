package com.example.todoappkotlin.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*

@Composable
fun AddFloatingBtn(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier

        ){
        Text(text = "+")
    }
}