package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumberMemoryScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var currentNumber by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var isShowingNumber by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isShowingNumber) {
            Text(
                text = currentNumber,
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Enter the number") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (userInput == currentNumber) {
                        score++
                        currentNumber = generateNumber(score + 2)
                        userInput = ""
                    } else {
                        onFinish(score)
                    }
                    isShowingNumber = true
                }
            ) {
                Text("Submit")
            }
        }
    }
}

private fun generateNumber(length: Int): String {
    return (1..length).map { (0..9).random() }.joinToString("")
} 