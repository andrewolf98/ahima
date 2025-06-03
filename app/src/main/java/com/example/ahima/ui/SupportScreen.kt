package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.data.CountryData
import com.example.ahima.support.AiSupportChat

@Composable
fun SupportScreen() {
    var userCountry by remember { mutableStateOf("Россия") }
    val contact = CountryData.getContactFor(userCountry)

    var userMessage by remember { mutableStateOf("") }
    var aiReply by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Поддержка", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Контакт в вашей стране: ${contact?.mentalHealthLine ?: "Нет данных"}")

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = userMessage,
            onValueChange = { userMessage = it },
            label = { Text("Ваш вопрос") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            aiReply = AiSupportChat.processUserMessage(userMessage)
        }) {
            Text("Отправить")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Ответ ИИ: $aiReply")
    }
}
