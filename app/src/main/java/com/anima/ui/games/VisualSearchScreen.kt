package com.anima.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun VisualSearchScreen(
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var currentGrid by remember { mutableStateOf(List(9) { "O" }) }
    var targetPresent by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Find the 'X' in the grid",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Display 3x3 grid
        Column {
            for (i in 0..2) {
                Row {
                    for (j in 0..2) {
                        Text(
                            text = currentGrid[i * 3 + j],
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    isCorrect = targetPresent
                    updateScore()
                }
            ) {
                Text("Present")
            }

            Button(
                onClick = {
                    isCorrect = !targetPresent
                    updateScore()
                }
            ) {
                Text("Not Present")
            }
        }
    }
}

private fun updateScore() {
    if (isCorrect) {
        score++
    }
    // Generate new grid
    val (grid, present) = generateGrid()
    currentGrid = grid
    targetPresent = present
}

private fun generateGrid(): Pair<List<String>, Boolean> {
    val grid = List(9) { "O" }
    val hasTarget = Random.nextBoolean()
    if (hasTarget) {
        val targetIndex = Random.nextInt(9)
        grid[targetIndex] = "X"
    }
    return Pair(grid, hasTarget)
} 