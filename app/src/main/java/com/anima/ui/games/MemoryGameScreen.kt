package com.anima.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.games.monitoring.MemoryGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MemoryGameScreen(
    game: MemoryGame,
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
                text = "Игра на память",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Счет и раунд
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
                    text = "Раунд: ${state.round}/10",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Последовательность
            if (state.isShowingSequence) {
                Text(
                    text = state.sequence.joinToString(" "),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
            
            // Кнопки с числами
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0..2) {
                            val number = row * 3 + col + 1
                            Button(
                                onClick = { game.onNumberClick(number) },
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                enabled = !state.isShowingSequence && !state.isGameOver
                            ) {
                                Text(
                                    text = number.toString(),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    }
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