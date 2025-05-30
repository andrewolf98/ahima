package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WordAssociationScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentWord by remember { mutableStateOf("")
    var userInput by remember { mutableStateOf("") }
    var isShowingWord by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isShowingWord) {
            Text(
                text = currentWord,
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Enter associated word") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    score++
                    currentWord = generateWord()
                    userInput = ""
                    isShowingWord = true
                }
            ) {
                Text("Submit")
            }
        }
    }
}

private fun generateWord(): String {
    val words = listOf(
        "happy", "sad", "angry", "calm", "excited",
        "tired", "energetic", "peaceful", "anxious", "relaxed"
    )
    return words.random()
} 