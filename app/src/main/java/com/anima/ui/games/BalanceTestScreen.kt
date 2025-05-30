package com.anima.ui.games

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.games.monitoring.BalanceTestGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BalanceTestScreen(
    game: BalanceTestGame,
    onGameComplete: () -> Unit
) {
    val state = game.state
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        game.startGame()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            Text(
                text = "Баланс шарика",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Счет и время
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Счет: ${state.score}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Время: ${state.timeRemaining / 1000}с",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Игровое поле
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            val position = Offset(
                                change.position.x / size.width,
                                change.position.y / size.height
                            )
                            game.updateBallPosition(position)
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Целевая точка
                    drawCircle(
                        color = Color.Green,
                        radius = 20f,
                        center = Offset(
                            state.targetPosition.x * size.width,
                            state.targetPosition.y * size.height
                        )
                    )
                    
                    // Шарик
                    drawCircle(
                        color = Color.Red,
                        radius = 30f,
                        center = Offset(
                            state.ballPosition.x * size.width,
                            state.ballPosition.y * size.height
                        )
                    )
                    
                    // Ветер
                    drawLine(
                        color = Color.Blue,
                        start = Offset(
                            state.ballPosition.x * size.width,
                            state.ballPosition.y * size.height
                        ),
                        end = Offset(
                            (state.ballPosition.x + state.windForce.x) * size.width,
                            (state.ballPosition.y + state.windForce.y) * size.height
                        ),
                        strokeWidth = 2f
                    )
                }
            }
            
            // Обратная связь
            if (state.feedback.isNotEmpty()) {
                Text(
                    text = state.feedback,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
            
            if (state.isGameOver) {
                Button(
                    onClick = onGameComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text("Завершить")
                }
            }
        }
    }
} 