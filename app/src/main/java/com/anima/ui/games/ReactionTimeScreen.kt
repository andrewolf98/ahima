package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ReactionTimeScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var isWaiting by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var reactionTime by remember { mutableStateOf(0L) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isWaiting) {
            Text(
                text = "Wait for the color to change...",
                style = MaterialTheme.typography.headlineMedium
            )
        } else {
            Button(
                onClick = {
                    startTime = System.currentTimeMillis()
                    isWaiting = true
                }
            ) {
                Text("Start")
            }
        }

        if (reactionTime > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Reaction time: ${reactionTime}ms",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    LaunchedEffect(isWaiting) {
        if (isWaiting) {
            delay(Random.nextLong(1000, 5000))
            isWaiting = false
            reactionTime = System.currentTimeMillis() - startTime
            score++
            if (score >= 5) {
                onFinish(score)
            }
        }
    }
} 