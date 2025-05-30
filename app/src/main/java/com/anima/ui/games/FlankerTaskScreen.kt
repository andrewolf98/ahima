package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FlankerTaskScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentStimulus by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Press left for <, right for >",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = currentStimulus,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    isCorrect = currentStimulus.contains("<")
                    updateScore()
                }
            ) {
                Text("Left")
            }

            Button(
                onClick = {
                    isCorrect = currentStimulus.contains(">")
                    updateScore()
                }
            ) {
                Text("Right")
            }
        }
    }
}

private fun updateScore() {
    if (isCorrect) {
        score++
    }
    // Generate new stimulus
    currentStimulus = generateStimulus()
}

private fun generateStimulus(): String {
    val directions = listOf("<", ">")
    val center = directions.random()
    val flankers = List(2) { directions.random() }
    return flankers.joinToString("") + center + flankers.joinToString("")
} 