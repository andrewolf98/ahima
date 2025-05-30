package com.anima.ui.games

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpotTheDifferenceScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Level $currentLevel",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Placeholder for images
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .weight(1f)
            ) {
                // Image 1
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .weight(1f)
            ) {
                // Image 2
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                score++
                currentLevel++
                if (currentLevel > 5) {
                    onFinish(score)
                }
            }
        ) {
            Text("Found Difference")
        }
    }
} 