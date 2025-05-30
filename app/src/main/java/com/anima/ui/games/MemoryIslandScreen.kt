package com.anima.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.data.therapeutic.TherapeuticGameType
import com.anima.games.therapeutic.TherapeuticGameFactory
import com.anima.games.therapeutic.games.MemoryItem
import com.anima.games.therapeutic.games.MemoryItemType
import com.anima.games.therapeutic.games.MemoryIslandGame

@Composable
fun MemoryIslandScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.MEMORY_ISLAND,
        TherapeuticGameConfigs.memoryIsland
    ) as MemoryIslandGame }
    
    val islandState by game.islandState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    
    LaunchedEffect(Unit) {
        game.start()
    }
    
    LaunchedEffect(game.isFinished()) {
        if (game.isFinished()) {
            onGameComplete(game.getScore())
        }
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
            // Верхняя панель с информацией
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Уровень: ${islandState.level}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Счет: ${islandState.score}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            // Область с предметами
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                islandState.items.forEach { item ->
                                    val itemPosition = Offset(
                                        x = item.position.first * size.width,
                                        y = item.position.second * size.height
                                    )
                                    if ((offset - itemPosition).getDistance() < 40f) {
                                        game.onItemSelected(item.id)
                                    }
                                }
                            }
                        }
                ) {
                    // Рисуем фон острова
                    drawCircle(
                        color = Color(0xFF81C784),
                        radius = size.width.coerceAtMost(size.height) * 0.4f,
                        center = Offset(size.width / 2, size.height / 2)
                    )
                    
                    // Рисуем предметы
                    islandState.items.forEach { item ->
                        val position = Offset(
                            x = item.position.first * size.width,
                            y = item.position.second * size.height
                        )
                        
                        // Рисуем фон предмета
                        drawCircle(
                            color = if (item.isMatched) 
                                Color(0xFF4CAF50) else Color(0xFF9E9E9E),
                            radius = 40f,
                            center = position
                        )
                        
                        // Рисуем иконку предмета, если он открыт
                        if (item.isRevealed || item.isMatched) {
                            val iconColor = when (item.type) {
                                MemoryItemType.FLOWER -> Color(0xFFE91E63)
                                MemoryItemType.SHELL -> Color(0xFFFFC107)
                                MemoryItemType.STAR -> Color(0xFFFFEB3B)
                                MemoryItemType.CRYSTAL -> Color(0xFF2196F3)
                                MemoryItemType.LEAF -> Color(0xFF4CAF50)
                                MemoryItemType.FEATHER -> Color(0xFF9C27B0)
                            }
                            
                            drawCircle(
                                color = iconColor,
                                radius = 30f,
                                center = position
                            )
                        }
                    }
                }
            }
            
            // Нижняя панель с прогрессом
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (islandState.feedback.isNotEmpty()) {
                    Text(
                        text = islandState.feedback,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                LinearProgressIndicator(
                    progress = game.getProgress(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Осталось времени: ${remainingTime / 1000} сек",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
} 