package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntonationRecognitionScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentPhrase by remember { mutableStateOf("") }
    var isPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Recognize the emotion in the voice",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentPhrase,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                isPlaying = true
                // Play audio here
            }
        ) {
            Text("Play")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EmotionButton("Happy") { checkAnswer("happy") }
            EmotionButton("Sad") { checkAnswer("sad") }
            EmotionButton("Angry") { checkAnswer("angry") }
            EmotionButton("Neutral") { checkAnswer("neutral") }
        }
    }
}

@Composable
private fun EmotionButton(
    emotion: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(emotion)
    }
}

private fun checkAnswer(emotion: String) {
    // Check if the answer is correct
    score++
    // Generate new phrase
    currentPhrase = generatePhrase()
} 