package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.scheduler.TaskScheduler
import com.example.ahima.storage.LocalStorage

@Composable
fun TaskScheduleScreen() {
    val profile = LocalStorage.getProfile()
    val schedule = TaskScheduler.getSchedule(profile)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Расписание задач", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        for (task in schedule) {
            Text("${task.time} — ${task.title}")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
