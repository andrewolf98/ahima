package com.anima.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.games.monitoring.RhythmTestGame

@Composable
fun RhythmTestScreen(
    game: RhythmTestGame,
    onGameComplete: () -> Unit
) {
    val state = game.state
    
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
                text = "Ритм-тест",
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
            
            // Паттерн
            Text(
                text = state.pattern,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 32.dp)
            )
            
            // Кнопки управления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { game.onTap() },
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                ) {
                    Text("X")
                }
                
                Button(
                    onClick = { game.onRest() },
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                ) {
                    Text("-")
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