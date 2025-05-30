package com.anima.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.games.StroopTestGame
import com.anima.ui.components.*
import com.anima.viewmodel.DiagnosticGamesViewModel

@Composable
fun StroopTestScreen(
    viewModel: DiagnosticGamesViewModel = viewModel()
) {
    val game = viewModel.currentGame.collectAsState().value as? StroopTestGame
    val score by viewModel.score
    val total by viewModel.total
    val isFinished by viewModel.isFinished
    val error by viewModel.error

    var showResultDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isFinished) {
        if (isFinished) {
            showResultDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameScore(score = score, total = total)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Отображение текущего задания
        game?.getCurrentTask()?.let { task ->
            Text(
                text = task.слово,
                color = when (task.цвет) {
                    "красный" -> Color.Red
                    "синий" -> Color.Blue
                    "зеленый" -> Color.Green
                    else -> Color.Black
                },
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Кнопки выбора цвета
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("красный", "синий", "зеленый").forEach { color ->
                GameButton(
                    text = color,
                    onClick = { viewModel.submitAnswer(color) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // Диалог с результатами
    if (showResultDialog) {
        GameResultDialog(
            score = score,
            total = total,
            timeSpent = game?.getTimeSpent() ?: 0,
            onDismiss = { showResultDialog = false },
            onRestart = {
                showResultDialog = false
                viewModel.resetGame()
            }
        )
    }

    // Диалог с ошибкой
    error?.let { errorMessage ->
        GameErrorDialog(
            error = errorMessage,
            onDismiss = { viewModel.resetGame() }
        )
    }
} 