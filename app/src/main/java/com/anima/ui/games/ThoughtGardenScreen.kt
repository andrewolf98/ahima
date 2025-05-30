package com.anima.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.data.therapeutic.TherapeuticGameType
import com.anima.games.therapeutic.TherapeuticGameFactory
import com.anima.games.therapeutic.games.Thought
import com.anima.games.therapeutic.games.ThoughtGardenGame

@Composable
fun ThoughtGardenScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.THOUGHT_GARDEN,
        TherapeuticGameConfigs.thoughtGarden
    ) as ThoughtGardenGame }
    
    val gardenState by game.gardenState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    val textMeasurer = rememberTextMeasurer()
    
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
        // Сад с мыслями
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            gardenState.thoughts.forEach { thought ->
                val position = Offset(
                    x = thought.position.first * size.width,
                    y = thought.position.second * size.height
                )
                
                // Рисуем фон мысли
                drawCircle(
                    color = if (thought.isPositive) 
                        Color(0xFF81C784) else Color(0xFFE57373),
                    radius = 100f,
                    center = position
                )
                
                // Рисуем текст мысли
                drawText(
                    textMeasurer = textMeasurer,
                    text = thought.text,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    ),
                    topLeft = Offset(
                        x = position.x - 90f,
                        y = position.y - 20f
                    )
                )
                
                // Рисуем иконку действия
                val iconOffset = Offset(
                    x = position.x + 80f,
                    y = position.y + 80f
                )
                
                if (thought.isPositive) {
                    drawCircle(
                        color = Color(0xFF4FC3F7),
                        radius = 20f,
                        center = iconOffset
                    )
                } else {
                    drawCircle(
                        color = Color(0xFF9E9E9E),
                        radius = 20f,
                        center = iconOffset
                    )
                }
            }
        }
        
        // Обработка нажатий
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        gardenState.thoughts.forEach { thought ->
                            val thoughtPosition = Offset(
                                x = thought.position.first * size.width,
                                y = thought.position.second * size.height
                            )
                            
                            val iconOffset = Offset(
                                x = thoughtPosition.x + 80f,
                                y = thoughtPosition.y + 80f
                            )
                            
                            // Проверяем, нажали ли на иконку действия
                            if (offset.x >= iconOffset.x - 20f && 
                                offset.x <= iconOffset.x + 20f &&
                                offset.y >= iconOffset.y - 20f && 
                                offset.y <= iconOffset.y + 20f) {
                                game.onThoughtAction(thought.id, thought.isPositive)
                            }
                        }
                    }
                }
        )
        
        // Нижняя панель с прогрессом
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Счет: ${game.getScore()}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
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