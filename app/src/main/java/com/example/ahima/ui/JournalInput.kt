package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.journal.JournalEntry
import com.example.ahima.journal.JournalManager

@Composable
fun JournalInputScreen() {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Запись в дневник", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Ваши мысли...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val currentDate = java.time.LocalDate.now().toString()
            JournalManager.addEntry(JournalEntry(currentDate, text))
            text = ""
        }) {
            Text("Сохранить запись")
        }
    }
}
