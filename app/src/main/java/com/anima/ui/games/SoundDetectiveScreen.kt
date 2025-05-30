package com.anima.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.data.therapeutic.TherapeuticGameType
import com.anima.games.therapeutic.TherapeuticGameFactory
import com.anima.games.therapeutic.games.Sound
import com.anima.games.therapeutic.games.SoundDetectiveGame
import com.anima.games.therapeutic.games.SoundCategory

@Composable
fun SoundDetectiveScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.SOUND_DETECTIVE,
        TherapeuticGameConfigs.soundDetective
    ) as SoundDetectiveGame }
    
    val detectiveState by game.detectiveState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    
    var playingSoundId by remember { mutableStateOf<Int?>(null) }
    
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
            // Верхняя панель с инструкциями
            Text(
                text = "Найдите звук: ${detectiveState.targetSound?.description ?: ""}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Сетка звуков
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                detectiveState.sounds.forEach { sound ->
                    SoundCard(
                        sound = sound,
                        isPlaying = sound.id == playingSoundId,
                        onPlayClick = {
                            playingSoundId = if (playingSoundId == sound.id) null else sound.id
                        },
                        onSelectClick = {
                            game.onSoundSelected(sound.id)
                        }
                    )
                }
            }
            
            // Нижняя панель с прогрессом
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (detectiveState.feedback.isNotEmpty()) {
                    Text(
                        text = detectiveState.feedback,
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
}

@Composable
private fun SoundCard(
    sound: Sound,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onSelectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (sound.category) {
                SoundCategory.NATURE -> Color(0xFF81C784)
                SoundCategory.MUSIC -> Color(0xFF4FC3F7)
                SoundCategory.ANIMALS -> Color(0xFFFFB74D)
                SoundCategory.CITY -> Color(0xFF9575CD)
                SoundCategory.HOUSEHOLD -> Color(0xFFE57373)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = sound.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = sound.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Кнопка воспроизведения
                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Остановить" else "Воспроизвести",
                        tint = Color.White
                    )
                }
                
                // Кнопка выбора
                Button(
                    onClick = onSelectClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = when (sound.category) {
                            SoundCategory.NATURE -> Color(0xFF81C784)
                            SoundCategory.MUSIC -> Color(0xFF4FC3F7)
                            SoundCategory.ANIMALS -> Color(0xFFFFB74D)
                            SoundCategory.CITY -> Color(0xFF9575CD)
                            SoundCategory.HOUSEHOLD -> Color(0xFFE57373)
                        }
                    )
                ) {
                    Text("Выбрать")
                }
            }
        }
    }
} 