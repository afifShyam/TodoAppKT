package com.example.todoappkotlin

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todoappkotlin.component.TodoListItem
import com.example.todoappkotlin.model.TodoModel
import com.example.todoappkotlin.util.ListState
import com.example.todoappkotlin.util.TodoFilter

@Composable
fun TodoListPage(
    todos: List<TodoModel>,
    modifier: Modifier = Modifier,
    onDelete: (TodoModel) -> Unit,
    onToggleComplete: (TodoModel) -> Unit,
    onEdit: (TodoModel) -> Unit,
    onAddClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf(TodoFilter.All) }
    var searchBounds by remember { mutableStateOf<Rect?>(null) }

    val trimmedQuery = searchQuery.trim()
    val doneCount = remember(todos) { todos.count { it.isDone } }
    val activeCount = todos.size - doneCount
    val filteredTodos = remember(todos, trimmedQuery, filter) {
        val hasQuery = trimmedQuery.isNotBlank()
        todos.filter { todo ->
            val matchesFilter = when (filter) {
                TodoFilter.All -> true
                TodoFilter.Active -> !todo.isDone
                TodoFilter.Done -> todo.isDone
            }
            val matchesQuery = !hasQuery ||
                todo.title.contains(trimmedQuery, ignoreCase = true) ||
                todo.description.contains(trimmedQuery, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }
    val listState = when {
        todos.isEmpty() -> ListState.Empty
        filteredTodos.isEmpty() -> ListState.NoMatches
        else -> ListState.List
    }
    val showControls = todos.isNotEmpty() || trimmedQuery.isNotEmpty() || filter != TodoFilter.All

    LaunchedEffect(showControls) {
        if (!showControls) {
            searchBounds = null
        }
    }

    val clearFocusModifier = Modifier.pointerInput(searchBounds) {
        awaitEachGesture{
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val bounds = searchBounds
                    if (bounds != null && !bounds.contains(down.position)) {
                        focusManager.clearFocus()
                    }
        }

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .then(clearFocusModifier),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (showControls) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search tasks") },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        searchBounds = coordinates.boundsInParent()
                    }
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TodoFilter.entries.forEach { option ->
                    val label = when (option) {
                        TodoFilter.All -> "All (${todos.size})"
                        TodoFilter.Active -> "Active ($activeCount)"
                        TodoFilter.Done -> "Done ($doneCount)"
                    }
                    FilterChip(
                        selected = filter == option,
                        onClick = { filter = option },
                        label = { Text(label) }
                    )
                }
            }
        }

        Crossfade(
            targetState = listState,
            label = "todoState"
        ) { state ->
            when (state) {
                ListState.Empty -> EmptyState(onAddClick = onAddClick)
                ListState.NoMatches -> FilteredEmptyState(
                    searchQuery = trimmedQuery,
                    filter = filter,
                    onClearFilters = {
                        searchQuery = ""
                        filter = TodoFilter.All
                    }
                )
                ListState.List -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp, top = 4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = filteredTodos,
                        key = { it.id }
                    ) { todo ->
                        TodoListItem(
                            item = todo,
                            onDelete = { onDelete(todo) },
                            onToggleComplete = { onToggleComplete(todo) },
                            onEdit = { onEdit(todo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.Checklist,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Nothing yet.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Add your first task and keep your day on track.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(onClick = onAddClick) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Create task")
            }
        }
    }
}

@Composable
private fun FilteredEmptyState(
    searchQuery: String,
    filter: TodoFilter,
    onClearFilters: () -> Unit
) {
    val headline = if (searchQuery.isNotBlank()) {
        "No results for \"$searchQuery\""
    } else {
        "No tasks match this filter."
    }
    val helperText = if (filter != TodoFilter.All && searchQuery.isNotBlank()) {
        "Try clearing search or switching filters."
    } else if (filter != TodoFilter.All) {
        "Try switching to All tasks."
    } else {
        "Try a different keyword."
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = headline,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            OutlinedButton(onClick = onClearFilters) {
                Text(text = "Clear filters")
            }
        }
    }
}
