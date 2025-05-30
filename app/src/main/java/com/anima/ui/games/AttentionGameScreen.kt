package com.anima.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.games.monitoring.AttentionGame
import com.anima.games.monitoring.Target

@Composable
fun AttentionGameScreen(
    game: AttentionGame,
    onGameComplete: () -> Unit
) {
    val state = game.state
    
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
                text = "Игра на внимание",
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
            
            // Игровая сетка
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0 until 5) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0 until 5) {
                            val target = state.targets.find { 
                                it.position.first == row && it.position.second == col 
                            }
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            target == null -> Color.Gray
                                            target.isTarget -> Color.Green
                                            else -> Color.Red
                                        }
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = if (target?.id in state.userSelections) 
                                            Color.Yellow else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                if (target != null) {
                                    Text(
                                        text = target.id.toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
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
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            
            if (state.isGameOver) {
                Button(
                    onClick = onGameComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Завершить")
                }
            }
        }
    }
} 