package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.storage.SettingsStorage

@Composable
fun SettingsScreen() {
    var language by remember { mutableStateOf(SettingsStorage.language) }
    var notificationsEnabled by remember { mutableStateOf(SettingsStorage.notificationsEnabled) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Настройки", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = language,
            onValueChange = { language = it },
            label = { Text("Язык") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Уведомления")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            SettingsStorage.language = language
            SettingsStorage.notificationsEnabled = notificationsEnabled
        }) {
            Text("Сохранить")
        }
    }
}
