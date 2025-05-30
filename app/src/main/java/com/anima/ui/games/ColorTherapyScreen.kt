package com.anima.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.data.therapeutic.TherapeuticGameType
import com.anima.games.therapeutic.TherapeuticGameFactory
import com.anima.games.therapeutic.games.ColorTask
import com.anima.games.therapeutic.games.Emotion
import com.anima.games.therapeutic.games.ColorTherapyGame

@Composable
fun ColorTherapyScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.COLOR_THERAPY,
        TherapeuticGameConfigs.colorTherapy
    ) as ColorTherapyGame }
    
    val therapyState by game.therapyState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    
    var selectedColor by remember { mutableStateOf(Color.White) }
    
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
            Text(
                text = "Цветотерапия",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Счет: ${therapyState.score}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Список задач
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                therapyState.tasks.forEach { task ->
                    TaskCard(
                        task = task,
                        isSelected = task.id == therapyState.currentTask?.id,
                        onClick = { game.onTaskSelected(task.id) }
                    )
                }
            }
            
            // Цветовая палитра
            if (therapyState.currentTask != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Выберите цвет для ${getEmotionText(therapyState.currentTask.emotion)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Цветовая палитра
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ColorPaletteItem(
                            color = Color(0xFF81C784), // Зеленый
                            isSelected = selectedColor == Color(0xFF81C784),
                            onClick = { selectedColor = Color(0xFF81C784) }
                        )
                        ColorPaletteItem(
                            color = Color(0xFFFFB74D), // Оранжевый
                            isSelected = selectedColor == Color(0xFFFFB74D),
                            onClick = { selectedColor = Color(0xFFFFB74D) }
                        )
                        ColorPaletteItem(
                            color = Color(0xFFFFEB3B), // Желтый
                            isSelected = selectedColor == Color(0xFFFFEB3B),
                            onClick = { selectedColor = Color(0xFFFFEB3B) }
                        )
                        ColorPaletteItem(
                            color = Color(0xFF64B5F6), // Синий
                            isSelected = selectedColor == Color(0xFF64B5F6),
                            onClick = { selectedColor = Color(0xFF64B5F6) }
                        )
                        ColorPaletteItem(
                            color = Color(0xFFBA68C8), // Фиолетовый
                            isSelected = selectedColor == Color(0xFFBA68C8),
                            onClick = { selectedColor = Color(0xFFBA68C8) }
                        )
                    }
                    
                    Button(
                        onClick = { game.onColorSelected(selectedColor) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Выбрать цвет")
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
                if (therapyState.feedback.isNotEmpty()) {
                    Text(
                        text = therapyState.feedback,
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

@Composable
private fun TaskCard(
    task: ColorTask,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = when {
                task.isCompleted -> Color(0xFF81C784)
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = getEmotionText(task.emotion),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.isCompleted) Color.White else MaterialTheme.colorScheme.onSurface
                )
                
                if (!task.isCompleted && !isSelected) {
                    Text(
                        text = "Найдите подходящий цвет",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            if (!task.isCompleted) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(task.targetColor)
                )
            }
        }
    }
}

@Composable
private fun ColorPaletteItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
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
}

private fun getEmotionText(emotion: Emotion): String = when (emotion) {
    Emotion.CALM -> "Спокойствие"
    Emotion.ENERGY -> "Энергия"
    Emotion.HAPPINESS -> "Радость"
    Emotion.FOCUS -> "Сосредоточенность"
    Emotion.CREATIVITY -> "Креативность"
} 