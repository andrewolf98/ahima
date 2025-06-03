package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.model.UserProfile
import com.example.ahima.storage.Prefs
import android.content.Context

@Composable
fun ProfileScreen(context: Context) {
    var phq9 by remember { mutableStateOf(0) }
    var gad7 by remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Профиль пользователя", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "PHQ-9:")
        TextField(
            value = phq9.toString(),
            onValueChange = { phq9 = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "GAD-7:")
        TextField(
            value = gad7.toString(),
            onValueChange = { gad7 = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            Prefs.saveProfile(context, UserProfile(phq9Score = phq9, gad7Score = gad7))
        }) {
            Text("Сохранить")
        }
    }
}
