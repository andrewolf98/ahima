package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("О приложении", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
        Text("AHIMA — персональный помощник для психологической самопомощи.")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Разработано командой из 2 человек: ты и ИИ.")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Все функции открыты, проект на стадии тестирования.")
    }
}
