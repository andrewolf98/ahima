package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.model.UserProfile
import com.example.ahima.storage.LocalStorage

@Composable
fun DiagnosticsScreen() {
    var phq9Input by remember { mutableStateOf("10") }
    var gad7Input by remember { mutableStateOf("7") }
    var savedProfile by remember { mutableStateOf(LocalStorage.getProfile()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Диагностика", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phq9Input,
            onValueChange = { phq9Input = it },
            label = { Text("PHQ-9") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gad7Input,
            onValueChange = { gad7Input = it },
            label = { Text("GAD-7") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            savedProfile = UserProfile(
                phq9Score = phq9Input.toIntOrNull() ?: 0,
                gad7Score = gad7Input.toIntOrNull() ?: 0
            )
        }) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Текущий профиль: PHQ-9=${savedProfile.phq9Score}, GAD-7=${savedProfile.gad7Score}")
    }
}
