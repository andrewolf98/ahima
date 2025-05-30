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
import com.anima.games.therapeutic.games.Beat
import com.anima.games.therapeutic.games.BeatType
import com.anima.games.therapeutic.games.RhythmMeditationGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RhythmMeditationScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.RHYTHM_MEDITATION,
        TherapeuticGameConfigs.rhythmMeditation
    ) as RhythmMeditationGame }
    
    val rhythmState by game.rhythmState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    val scope = rememberCoroutineScope()
    
    // Анимация движения битов
    val infiniteTransition = rememberInfiniteTransition()
    val beatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    LaunchedEffect(Unit) {
        game.start()
    }
    
    LaunchedEffect(game.isFinished()) {
        if (game.isFinished()) {
            onGameComplete(game.getScore())
        }
    }
    
    // Проверка пропущенных битов
    LaunchedEffect(beatOffset) {
        rhythmState.beats.forEach { beat ->
            if (!beat.isHit && beat.position + beatOffset >= 0.9f) {
                game.onBeatMissed(beat.id)
            }
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
                    text = "Комбо: ${rhythmState.combo}x",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Счет: ${rhythmState.score}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            // Область ритма
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
                                val hitLine = size.height * 0.8f
                                if (offset.y >= hitLine - 50f && offset.y <= hitLine + 50f) {
                                    rhythmState.beats.forEach { beat ->
                                        val beatX = (beat.position + beatOffset) * size.width
                                        if (offset.x >= beatX - 30f && offset.x <= beatX + 30f) {
                                            game.onBeatHit(beat.id)
                                        }
                                    }
                                }
                            }
                        }
                ) {
                    // Рисуем линию попадания
                    drawLine(
                        color = Color(0xFF4CAF50),
                        start = Offset(0f, size.height * 0.8f),
                        end = Offset(size.width, size.height * 0.8f),
                        strokeWidth = 4f
                    )
                    
                    // Рисуем биты
                    rhythmState.beats.forEach { beat ->
                        val x = (beat.position + beatOffset) * size.width
                        val y = size.height * 0.8f
                        
                        // Рисуем след бита
                        drawCircle(
                            color = when (beat.type) {
                                BeatType.NORMAL -> Color(0xFF2196F3)
                                BeatType.SPECIAL -> Color(0xFFFFC107)
                            }.copy(alpha = 0.3f),
                            radius = 30f,
                            center = Offset(x, y)
                        )
                        
                        // Рисуем сам бит
                        if (!beat.isHit) {
                            drawCircle(
                                color = when (beat.type) {
                                    BeatType.NORMAL -> Color(0xFF2196F3)
                                    BeatType.SPECIAL -> Color(0xFFFFC107)
                                },
                                radius = 20f,
                                center = Offset(x, y)
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
                if (rhythmState.feedback.isNotEmpty()) {
                    Text(
                        text = rhythmState.feedback,
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