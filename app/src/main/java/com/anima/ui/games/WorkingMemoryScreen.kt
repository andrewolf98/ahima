package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun WorkingMemoryScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentSequence by remember { mutableStateOf("") }
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
            text = "Remember the sequence in reverse order",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentSequence,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Your answer (in reverse)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isCorrect = checkAnswer(currentSequence, userInput)
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
    // Generate new sequence
    currentSequence = generateSequence()
    userInput = ""
}

private fun generateSequence(): String {
    val length = 3 + (score / 2) // Increase length as score increases
    return (1..length).map { Random.nextInt(1, 10) }.joinToString("")
}

private fun checkAnswer(sequence: String, answer: String): Boolean {
    val reversedSequence = sequence.reversed()
    return answer == reversedSequence
} 