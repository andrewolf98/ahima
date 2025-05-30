package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerbalFluencyScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentCategory by remember { mutableStateOf("") }
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
            text = "Name items in the category",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentCategory,
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
                isCorrect = checkAnswer(currentCategory, userInput)
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
    // Generate new category
    currentCategory = generateCategory()
    userInput = ""
}

private fun generateCategory(): String {
    val categories = listOf(
        "Animals",
        "Fruits",
        "Countries",
        "Colors",
        "Sports"
    )
    return categories.random()
}

private fun checkAnswer(category: String, answer: String): Boolean {
    val validAnswers = when (category) {
        "Animals" -> listOf("dog", "cat", "bird", "fish", "lion", "tiger", "elephant", "monkey")
        "Fruits" -> listOf("apple", "banana", "orange", "grape", "strawberry", "mango", "pineapple")
        "Countries" -> listOf("usa", "canada", "mexico", "brazil", "france", "germany", "japan")
        "Colors" -> listOf("red", "blue", "green", "yellow", "purple", "orange", "black", "white")
        "Sports" -> listOf("football", "basketball", "tennis", "soccer", "baseball", "golf", "swimming")
        else -> emptyList()
    }
    return validAnswers.contains(answer.lowercase())
} 