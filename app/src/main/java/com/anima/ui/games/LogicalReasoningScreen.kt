package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LogicalReasoningScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentProblem by remember { mutableStateOf("") }
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
            text = "Solve the logical problem",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentProblem,
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
                isCorrect = checkAnswer(currentProblem, userInput)
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
    // Generate new problem
    currentProblem = generateProblem()
    userInput = ""
}

private fun generateProblem(): String {
    val problems = listOf(
        "If all A are B and all B are C, then all A are C. True or False?",
        "If some A are B and all B are C, then some A are C. True or False?",
        "If no A are B and all B are C, then no A are C. True or False?",
        "If all A are B and some B are C, then some A are C. True or False?"
    )
    return problems.random()
}

private fun checkAnswer(problem: String, answer: String): Boolean {
    return when (problem) {
        "If all A are B and all B are C, then all A are C. True or False?" -> answer.lowercase() == "true"
        "If some A are B and all B are C, then some A are C. True or False?" -> answer.lowercase() == "true"
        "If no A are B and all B are C, then no A are C. True or False?" -> answer.lowercase() == "false"
        "If all A are B and some B are C, then some A are C. True or False?" -> answer.lowercase() == "true"
        else -> false
    }
} 