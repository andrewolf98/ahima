package com.example.ahima.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ahima.io.ProfileImportExport
import com.example.ahima.model.UserProfile

@Composable
fun ExportScreen() {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Экспорт и импорт", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            ProfileImportExport.exportProfile(context, UserProfile(10, 7))
            message = "Профиль экспортирован"
        }) {
            Text("Экспорт")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val profile = ProfileImportExport.importProfile(context)
            message = "Импортировано: PHQ-9=${profile.phq9Score}, GAD-7=${profile.gad7Score}"
        }) {
            Text("Импорт")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}
