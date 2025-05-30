package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StroopTestScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentWord by remember { mutableStateOf("") }
    var currentColor by remember { mutableStateOf(Color.Black) }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Name the color of the text",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentWord,
            color = currentColor,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ColorButton("Red", Color.Red) {
                isCorrect = currentColor == Color.Red
                updateScore()
            }
            ColorButton("Blue", Color.Blue) {
                isCorrect = currentColor == Color.Blue
                updateScore()
            }
            ColorButton("Green", Color.Green) {
                isCorrect = currentColor == Color.Green
                updateScore()
            }
        }
    }
}

@Composable
private fun ColorButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Text(text)
    }
}

private fun updateScore() {
    if (isCorrect) {
        score++
    }
    // Generate new word and color
    val (word, color) = generateStroopStimulus()
    currentWord = word
    currentColor = color
}

private fun generateStroopStimulus(): Pair<String, Color> {
    val words = listOf("Red", "Blue", "Green")
    val colors = listOf(Color.Red, Color.Blue, Color.Green)
    return Pair(words.random(), colors.random())
} 