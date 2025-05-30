package com.anima.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import com.anima.games.therapeutic.games.Emotion
import com.anima.games.therapeutic.games.EmotionPuzzleGame
import com.anima.games.therapeutic.games.FacePart
import com.anima.games.therapeutic.games.FacePartType

@Composable
fun EmotionPuzzleScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.EMOTION_PUZZLE,
        TherapeuticGameConfigs.emotionPuzzle
    ) as EmotionPuzzleGame }
    
    val puzzleState by game.puzzleState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    val textMeasurer = rememberTextMeasurer()
    
    var draggedPart by remember { mutableStateOf<FacePart?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    
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
        // Верхняя панель с инструкциями
        Text(
            text = "Соберите лицо, выражающее ${puzzleState.targetEmotion.name.lowercase()}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )
        
        // Область для сборки лица
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Рисуем контур лица
            drawCircle(
                color = Color(0xFFFFE0B2),
                radius = size.width * 0.3f,
                center = Offset(size.width / 2, size.height / 2)
            )
            
            // Рисуем части лица
            puzzleState.parts.forEach { part ->
                val position = if (part.id == draggedPart?.id) {
                    Offset(
                        x = part.position.first * size.width + dragOffset.x,
                        y = part.position.second * size.height + dragOffset.y
                    )
                } else {
                    Offset(
                        x = part.position.first * size.width,
                        y = part.position.second * size.height
                    )
                }
                
                // Рисуем часть лица
                drawCircle(
                    color = when (part.type) {
                        FacePartType.EYES -> Color(0xFF4A4A4A)
                        FacePartType.MOUTH -> Color(0xFFE91E63)
                        FacePartType.EYEBROWS -> Color(0xFF795548)
                    },
                    radius = 30f,
                    center = position
                )
                
                // Рисуем подсказку
                drawText(
                    textMeasurer = textMeasurer,
                    text = when (part.type) {
                        FacePartType.EYES -> "Глаза"
                        FacePartType.MOUTH -> "Рот"
                        FacePartType.EYEBROWS -> "Брови"
                    },
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp
                    ),
                    topLeft = Offset(
                        x = position.x - 20f,
                        y = position.y + 40f
                    )
                )
            }
        }
        
        // Обработка перетаскивания
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            puzzleState.parts.forEach { part ->
                                val partPosition = Offset(
                                    x = part.position.first * size.width,
                                    y = part.position.second * size.height
                                )
                                if ((offset - partPosition).getDistance() < 30f) {
                                    draggedPart = part
                                    dragOffset = Offset.Zero
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragOffset += dragAmount
                        },
                        onDragEnd = {
                            draggedPart?.let { part ->
                                val newPosition = Pair(
                                    (part.position.first * size.width + dragOffset.x) / size.width,
                                    (part.position.second * size.height + dragOffset.y) / size.height
                                )
                                game.onPartPlaced(part.id, newPosition)
                            }
                            draggedPart = null
                            dragOffset = Offset.Zero
                        }
                    )
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
            if (puzzleState.feedback.isNotEmpty()) {
                Text(
                    text = puzzleState.feedback,
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