package com.example.todoappkotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date

@Entity(tableName = "todo_table")
data class TodoModel(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var description: String,
    var isDone: Boolean,
    var createdAt: Date,
    var updatedAt: Date = Date.from(Instant.now()),
)
