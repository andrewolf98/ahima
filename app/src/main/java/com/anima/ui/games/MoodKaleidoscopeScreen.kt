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
import com.anima.data.monitoring.Mood
import com.anima.games.monitoring.MoodKaleidoscopeGame

@Composable
fun MoodKaleidoscopeScreen(
    game: MoodKaleidoscopeGame,
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
                text = "Настроение-калейдоскоп",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Выбор настроения
            Text(
                text = "Выберите настроение",
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
                Mood.values().forEach { mood ->
                    MoodButton(
                        mood = mood,
                        isSelected = mood == state.selectedMood,
                        onClick = { game.onMoodSelected(mood) },
                        getMoodColor = { game.getMoodColor(mood) },
                        getMoodText = { game.getMoodText(mood) }
                    )
                }
            }
            
            // Выбор цвета
            if (state.selectedMood != null) {
                Text(
                    text = "Выберите оттенок",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                ColorPalette(
                    baseColor = game.getMoodColor(state.selectedMood),
                    selectedColor = state.selectedColor,
                    onColorSelected = { game.onColorSelected(it) }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Интенсивность
                Text(
                    text = "Интенсивность: ${state.intensity}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Slider(
                    value = state.intensity.toFloat(),
                    onValueChange = { game.onIntensityChanged(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Кнопка сохранения
                Button(
                    onClick = { 
                        game.saveMood()
                        onGameComplete()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = state.selectedMood != null && state.selectedColor != null && !state.isSaved
                ) {
                    Text(
                        text = if (state.isSaved) "Сохранено!" else "Сохранить настроение",
                        style = MaterialTheme.typography.titleMedium
                    )
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
private fun MoodButton(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit,
    getMoodColor: () -> Color,
    getMoodText: () -> String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(getMoodColor())
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
            text = getMoodText(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun ColorPalette(
    baseColor: Color,
    selectedColor: Color?,
    onColorSelected: (Color) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Создаем 5 оттенков базового цвета
        val colors = listOf(
            baseColor.copy(alpha = 0.5f),
            baseColor.copy(alpha = 0.7f),
            baseColor,
            baseColor.copy(alpha = 0.8f),
            baseColor.copy(alpha = 0.9f)
        )
        
        colors.forEach { color ->
            ColorButton(
                color = color,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) }
            )
        }
    }
}

@Composable
private fun ColorButton(
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