package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Помощь", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Если у вас возникают трудности с использованием приложения, пожалуйста, проверьте FAQ или обратитесь в службу поддержки.")
        Spacer(modifier = Modifier.height(8.dp))
        Text("1. Как ввести данные? — Перейдите в раздел 'Диагностика'")
        Text("2. Где мои награды? — В разделе 'Награды'")
        Text("3. Как экспортировать данные? — См. 'Экспорт и импорт'")
    }
}
