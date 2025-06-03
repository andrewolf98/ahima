package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.logic.LevelGenerator
import com.example.ahima.storage.LocalStorage

@Composable
fun NeuroLevelScreen() {
    val profile = LocalStorage.getProfile()
    val levels = remember { LevelGenerator.generate(profile) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Нейроуровни", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(levels) { level ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(level.title, style = MaterialTheme.typography.subtitle1)
                        Text("Сложность: ${level.difficulty}")
                        Text("Стиль: ${level.style}")
                    }
                }
            }
        }
    }
}
