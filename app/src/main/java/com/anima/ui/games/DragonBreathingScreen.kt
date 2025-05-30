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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.games.therapeutic.TherapeuticGameFactory
import com.anima.games.therapeutic.games.BreathPhase
import com.anima.games.therapeutic.games.DragonBreathingGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DragonBreathingScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.DRAGON_BREATHING,
        TherapeuticGameConfigs.dragonBreathing
    ) as DragonBreathingGame }
    
    val breathState by game.breathState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    val scope = rememberCoroutineScope()
    
    var isTouching by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        game.start()
    }
    
    LaunchedEffect(breathState.phase) {
        when (breathState.phase) {
            BreathPhase.INHALE -> delay(4000) // 4 секунды
            BreathPhase.HOLD -> delay(7000)   // 7 секунд
            BreathPhase.EXHALE -> delay(8000) // 8 секунд
        }
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isTouching = true
                        game.onBreathInput(true)
                        tryAwaitRelease()
                        isTouching = false
                        game.onBreathInput(false)
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Верхняя панель с инструкциями
            Text(
                text = when (breathState.phase) {
                    BreathPhase.INHALE -> "Вдохните (4 секунды)"
                    BreathPhase.HOLD -> "Задержите дыхание (7 секунд)"
                    BreathPhase.EXHALE -> "Выдохните (8 секунд)"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
            
            // Дракон и огонь
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition = rememberInfiniteTransition()
                val rotation by infiniteTransition.animateFloat(
                    initialValue = -5f,
                    targetValue = 5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                
                Canvas(
                    modifier = Modifier
                        .size(300.dp)
                        .scale(breathState.fireSize)
                ) {
                    // Рисуем дракона
                    rotate(rotation) {
                        // Голова дракона
                        drawCircle(
                            color = Color(0xFF4CAF50),
                            radius = 50f,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                        
                        // Огонь
                        val firePath = Path().apply {
                            moveTo(size.width / 2 - 30f, size.height / 2 + 50f)
                            quadraticBezierTo(
                                size.width / 2,
                                size.height / 2 + 100f,
                                size.width / 2 + 30f,
                                size.height / 2 + 50f
                            )
                        }
                        
                        drawPath(
                            path = firePath,
                            color = if (breathState.isCorrect) 
                                Color(0xFFFF5722) else Color(0xFF9E9E9E),
                            style = Stroke(width = 20f)
                        )
                    }
                }
            }
            
            // Нижняя панель с прогрессом
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
} 