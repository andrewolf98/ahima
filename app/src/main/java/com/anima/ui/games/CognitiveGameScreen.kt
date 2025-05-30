package com.anima.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.anima.games.monitoring.CognitiveGame
import com.anima.games.monitoring.CognitiveGameType

@Composable
fun CognitiveGameScreen(
    game: CognitiveGame,
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
                text = when (state.gameType) {
                    CognitiveGameType.PUZZLE -> "Пазл"
                    CognitiveGameType.LOGIC -> "Логическая задача"
                    CognitiveGameType.PATTERN_RECOGNITION -> "Распознавание паттернов"
                    CognitiveGameType.SPATIAL_REASONING -> "Пространственное мышление"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Счет и раунд
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Счет: ${state.score}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Раунд: ${state.round}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Головоломка
            when (state.gameType) {
                CognitiveGameType.PUZZLE -> {
                    // Сетка для скользящего пазла
                    val gridSize = 3
                    val cellSize = 100.dp
                    
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (row in 0 until gridSize) {
                            Row {
                                for (col in 0 until gridSize) {
                                    val index = row * gridSize + col
                                    val number = state.puzzle.grid[index]
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(cellSize)
                                            .padding(2.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (number == 0) 
                                                    MaterialTheme.colorScheme.surfaceVariant
                                                else 
                                                    MaterialTheme.colorScheme.primaryContainer
                                            )
                                            .clickable { game.onNumberClick(number) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (number != 0) {
                                            Text(
                                                text = number.toString(),
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                CognitiveGameType.LOGIC -> {
                    // Логическая задача
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = state.puzzle.description,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            // Варианты ответов
                            state.puzzle.options.forEach { option ->
                                Button(
                                    onClick = { game.onNumberClick(option.toInt()) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(option)
                                }
                            }
                        }
                    }
                }
                
                CognitiveGameType.PATTERN_RECOGNITION -> {
                    // Паттерны
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Отображение паттерна
                        Row(
                            modifier = Modifier.padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            state.puzzle.pattern.forEach { number ->
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = number.toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                        
                        // Варианты ответов
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            state.puzzle.options.forEach { option ->
                                Button(
                                    onClick = { game.onNumberClick(option.toInt()) },
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(option)
                                }
                            }
                        }
                    }
                }
                
                CognitiveGameType.SPATIAL_REASONING -> {
                    // Пространственная головоломка
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Отображение фигуры
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.puzzle.description,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Варианты ответов
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            state.puzzle.options.forEach { option ->
                                Button(
                                    onClick = { game.onNumberClick(option.toInt()) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(option)
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
            
            // Кнопка завершения
            if (state.isGameOver) {
                Button(
                    onClick = onGameComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Завершить")
                }
            }
        }
    }
} 