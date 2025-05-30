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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.data.therapeutic.TherapeuticGameType
import com.anima.games.therapeutic.TherapeuticGameFactory
import com.anima.games.therapeutic.games.SocialMazeGame
import com.anima.games.therapeutic.games.SocialSituation

@Composable
fun SocialMazeScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.SOCIAL_MAZE,
        TherapeuticGameConfigs.socialMaze
    ) as SocialMazeGame }
    
    val mazeState by game.mazeState.collectAsState()
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
        // Лабиринт с ситуациями
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Рисуем линии лабиринта
            drawLine(
                color = Color(0xFF9E9E9E),
                start = Offset(0f, size.height * 0.2f),
                end = Offset(size.width, size.height * 0.2f),
                strokeWidth = 2f
            )
            drawLine(
                color = Color(0xFF9E9E9E),
                start = Offset(0f, size.height * 0.4f),
                end = Offset(size.width, size.height * 0.4f),
                strokeWidth = 2f
            )
            drawLine(
                color = Color(0xFF9E9E9E),
                start = Offset(0f, size.height * 0.6f),
                end = Offset(size.width, size.height * 0.6f),
                strokeWidth = 2f
            )
            drawLine(
                color = Color(0xFF9E9E9E),
                start = Offset(0f, size.height * 0.8f),
                end = Offset(size.width, size.height * 0.8f),
                strokeWidth = 2f
            )
            
            // Рисуем ситуации
            mazeState.situations.forEach { situation ->
                val position = Offset(
                    x = situation.position.first * size.width,
                    y = situation.position.second * size.height
                )
                
                // Рисуем круг ситуации
                drawCircle(
                    color = if (situation.isCompleted) 
                        Color(0xFF81C784) else Color(0xFF4FC3F7),
                    radius = 40f,
                    center = position
                )
                
                // Рисуем номер ситуации
                drawText(
                    textMeasurer = textMeasurer,
                    text = situation.id.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp
                    ),
                    topLeft = Offset(
                        x = position.x - 10f,
                        y = position.y - 10f
                    )
                )
            }
        }
        
        // Обработка нажатий на ситуации
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        mazeState.situations.forEach { situation ->
                            val situationPosition = Offset(
                                x = situation.position.first * size.width,
                                y = situation.position.second * size.height
                            )
                            if ((offset - situationPosition).getDistance() < 40f) {
                                game.onSituationSelected(situation.id)
                            }
                        }
                    }
                }
        )
        
        // Диалог с ситуацией
        mazeState.currentSituation?.let { situation ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = situation.description,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    situation.options.forEach { option ->
                        Button(
                            onClick = { game.onResponseSelected(option.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option.text,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
        
        // Нижняя панель с прогрессом
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (mazeState.feedback.isNotEmpty()) {
                Text(
                    text = mazeState.feedback,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
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