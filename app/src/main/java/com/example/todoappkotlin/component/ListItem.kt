package com.example.todoappkotlin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoappkotlin.model.TodoModel
import java.text.SimpleDateFormat

@Composable
fun TodoListItem(item: TodoModel, onDelete: () -> Unit, onToggleComplete: () -> Unit) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
        .then(
            if (item.isDone) {
                Modifier.background(Color(0xFF4CAF50))
            } else if (item.isGradient) {
                Modifier.background(getGradientBrush(
                    startColor = item.gradientStart,
                    endColor = item.gradientEnd
                ))
            } else {
                Modifier.background(item.color.toComposeColor())
            }
        )
        .padding(16.dp)

    Row(
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val createdNow = SimpleDateFormat("hh:mm a").format(item.createdAt)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = createdNow, color = Color.LightGray, fontSize = 12.sp)
            Text(
                text = item.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(text = item.description, color = Color.White, fontSize = 14.sp)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = if (item.isDone) "Undo" else "Done",
                modifier = Modifier
                    .clickable { onToggleComplete() }
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(8.dp),
                color = Color.White
            )

            Text(
                text = "Delete",
                modifier = Modifier
                    .clickable { onDelete() }
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(8.dp),
                color = Color.Yellow
            )
        }
    }
}
