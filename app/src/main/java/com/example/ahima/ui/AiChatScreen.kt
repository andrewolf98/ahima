package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.support.AiSupportChat

@Composable
fun AiChatScreen() {
    var userInput by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Чат поддержки", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Ваше сообщение") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            response = AiSupportChat.processUserMessage(userInput)
        }) {
            Text("Отправить")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ответ: $response")
    }
}
