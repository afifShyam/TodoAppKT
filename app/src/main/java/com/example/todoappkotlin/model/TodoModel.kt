package com.example.todoappkotlin.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoappkotlin.component.generateRandColorInt
import java.time.Instant
import java.util.Date
import kotlin.random.Random

@Entity(tableName = "todo_table")
data class TodoModel(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var description: String,
    var isDone: Boolean,
    var createdAt: Date,
    var updatedAt: Date = Date.from(Instant.now()),
    var color: Int = generateRandColorInt(),
    var isGradient: Boolean = Random.nextBoolean(),
    var gradientStart: Int = generateRandColorInt(),
    var gradientEnd: Int = generateRandColorInt()
)