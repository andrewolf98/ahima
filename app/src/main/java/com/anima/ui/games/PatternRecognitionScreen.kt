package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun PatternRecognitionScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentPattern by remember { mutableStateOf(generatePattern()) }
    var userInput by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = "Complete the pattern",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentPattern,
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
                isCorrect = checkAnswer(currentPattern, userInput)
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
        if (score >= 5) {
            onFinish(score)
        }
    }
    // Generate new pattern
    currentPattern = generatePattern()
    userInput = ""
}

private fun generatePattern(): String {
    val patterns = listOf(
        "1, 2, 3, ?",
        "2, 4, 6, ?",
        "1, 3, 5, ?",
        "3, 6, 9, ?"
    )
    return patterns.random()
}

private fun checkAnswer(pattern: String, answer: String): Boolean {
    return when (pattern) {
        "1, 2, 3, ?" -> answer == "4"
        "2, 4, 6, ?" -> answer == "8"
        "1, 3, 5, ?" -> answer == "7"
        "3, 6, 9, ?" -> answer == "12"
        else -> false
    }
} 