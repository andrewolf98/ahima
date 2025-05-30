package com.anima.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.monitoring.Emotion
import com.anima.games.monitoring.EmotionalWritingGame

@Composable
fun EmotionalWritingScreen(
    game: EmotionalWritingGame,
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
            
            // Область для письма
            if (state.currentEmotion != null) {
                // Подсказка
                Text(
                    text = game.getEmotionPrompt(state.currentEmotion),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                // Текстовое поле
                OutlinedTextField(
                    value = state.text,
                    onValueChange = { game.updateText(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                
                // Кнопка сохранения
                Button(
                    onClick = { 
                        game.saveEntry()
                        onGameComplete()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    enabled = state.text.isNotEmpty() && !state.isSaved
                ) {
                    Text("Сохранить")
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