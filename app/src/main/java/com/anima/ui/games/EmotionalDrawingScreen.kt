package com.anima.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.anima.data.monitoring.Emotion
import com.anima.games.monitoring.EmotionalDrawingGame
import com.anima.games.monitoring.Point

@Composable
fun EmotionalDrawingScreen(
    game: EmotionalDrawingGame,
    onGameComplete: () -> Unit
) {
    val state = game.state
    var description by remember { mutableStateOf("") }
    
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
                text = "Эмоциональный дневник",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Выбор эмоции
            Text(
                text = "Выберите эмоцию",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Emotion.values().forEach { emotion ->
                    EmotionButton(
                        emotion = emotion,
                        isSelected = emotion == state.currentEmotion,
                        onClick = { game.onEmotionSelected(emotion) },
                        getEmotionColor = { game.getEmotionColor(emotion) },
                        getEmotionText = { game.getEmotionText(emotion) }
                    )
                }
            }
            
            // Область для рисования
            if (state.currentEmotion != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 16.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        game.startNewPath(
                                            game.getEmotionColor(state.currentEmotion),
                                            5f
                                        )
                                        game.addPointToPath(Point(offset.x, offset.y))
                                    },
                                    onDrag = { change, _ ->
                                        game.addPointToPath(Point(change.position.x, change.position.y))
                                    },
                                    onDragEnd = {
                                        game.finishPath()
                                    }
                                )
                            }
                    ) {
                        // Рисуем все пути
                        state.drawingPath.forEach { path ->
                            drawPath(
                                path = Path().apply {
                                    if (path.points.isNotEmpty()) {
                                        moveTo(path.points.first().x, path.points.first().y)
                                        path.points.drop(1).forEach { point ->
                                            lineTo(point.x, point.y)
                                        }
                                    }
                                },
                                color = path.color,
                                style = Stroke(width = path.strokeWidth)
                            )
                        }
                        
                        // Рисуем текущий путь
                        state.currentPath?.let { path ->
                            drawPath(
                                path = Path().apply {
                                    if (path.points.isNotEmpty()) {
                                        moveTo(path.points.first().x, path.points.first().y)
                                        path.points.drop(1).forEach { point ->
                                            lineTo(point.x, point.y)
                                        }
                                    }
                                },
                                color = path.color,
                                style = Stroke(width = path.strokeWidth)
                            )
                        }
                    }
                }
                
                // Описание рисунка
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Опишите свой рисунок") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                
                // Кнопки управления
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { game.clearDrawing() },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Очистить")
                    }
                    
                    Button(
                        onClick = { 
                            game.saveDrawing(description)
                            onGameComplete()
                        },
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        enabled = state.drawingPath.isNotEmpty() && description.isNotEmpty() && !state.isSaved
                    ) {
                        Text("Сохранить")
                    }
                }
                
                if (state.feedback.isNotEmpty()) {
                    Text(
                        text = state.feedback,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmotionButton(
    emotion: Emotion,
    isSelected: Boolean,
    onClick: () -> Unit,
    getEmotionColor: () -> Color,
    getEmotionText: () -> String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(getEmotionColor())
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    shape = CircleShape
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
        
        Text(
            text = getEmotionText(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
} 