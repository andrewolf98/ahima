package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ProcessingSpeedScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentNumber by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Add 7 to the number",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentNumber.toString(),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Your answer") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isCorrect = checkAnswer(currentNumber, userInput)
                updateScore()
            }
        ) {
            Text("Submit")
        }
    }
}

private fun updateScore() {
    if (isCorrect) {
        score++
    }
    // Generate new number
    currentNumber = generateNumber()
    userInput = ""
}

private fun generateNumber(): Int {
    return Random.nextInt(1, 100)
}

private fun checkAnswer(number: Int, answer: String): Boolean {
    return try {
        answer.toInt() == number + 7
    } catch (e: NumberFormatException) {
        false
    }
} 