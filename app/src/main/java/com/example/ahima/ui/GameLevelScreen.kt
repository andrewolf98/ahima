package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.engine.GameLevelGenerator
import com.example.ahima.storage.LocalStorage

@Composable
fun GameLevelScreen() {
    val profile = LocalStorage.getProfile()
    val level = GameLevelGenerator.generate(profile)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Игровой уровень", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Сложность: ${level.difficulty}")
        Text("Тема: ${level.theme}")
        Text("Испытание: ${level.challenge}")
    }
}
