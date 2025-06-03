package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.tasks.DailyTaskEngine
import com.example.ahima.storage.LocalStorage

@Composable
fun DailyTaskScreen() {
    val profile = LocalStorage.getProfile()
    val tasks = remember { DailyTaskEngine.generateTasks(profile) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ежедневные задания", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(task.category, style = MaterialTheme.typography.subtitle1)
                        Text(task.description)
                    }
                }
            }
        }
    }
}
