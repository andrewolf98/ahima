package com.anima.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.games.monitoring.SleepQuestGame
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SleepQuestScreen(
    game: SleepQuestGame,
    onGameComplete: () -> Unit
) {
    val state = game.state
    var showBedTimePicker by remember { mutableStateOf(false) }
    var showWakeTimePicker by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            Text(
                text = "Квест сна",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Время отхода ко сну
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Время отхода ко сну",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Button(
                        onClick = { showBedTimePicker = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = state.bedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Выбрать время"
                        )
                    }
                }
            }
            
            // Время пробуждения
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Время пробуждения",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Button(
                        onClick = { showWakeTimePicker = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = state.wakeTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Выбрать время"
                        )
                    }
                }
            }
            
            // Качество сна
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Качество сна: ${state.quality}/10",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Slider(
                        value = state.quality.toFloat(),
                        onValueChange = { game.setQuality(it.toInt()) },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
            
            // Серия хорошего сна
            if (state.streak > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Ваша серия хорошего сна: ${state.streak} дней!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Советы по сну
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Советы по улучшению сна",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    game.getSleepTips().forEach { tip ->
                        Text(
                            text = "• $tip",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
            
            // Обратная связь
            if (state.feedback.isNotEmpty()) {
                Text(
                    text = state.feedback,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            
            // Кнопка сохранения
            Button(
                onClick = { 
                    game.saveEntry()
                    onGameComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = state.bedTime != null && state.wakeTime != null && !state.isSaved
            ) {
                Text("Сохранить")
            }
        }
    }
    
    // Пикеры времени
    if (showBedTimePicker) {
        TimePickerDialog(
            onDismiss = { showBedTimePicker = false },
            onTimeSelected = { time ->
                game.setBedTime(time)
                showBedTimePicker = false
            }
        )
    }
    
    if (showWakeTimePicker) {
        TimePickerDialog(
            onDismiss = { showWakeTimePicker = false },
            onTimeSelected = { time ->
                game.setWakeTime(time)
                showWakeTimePicker = false
            }
        )
    }
}

@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (LocalDateTime) -> Unit
) {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите время") },
        text = {
            Column {
                // Здесь должен быть TimePicker, но для простоты используем слайдеры
                Text("Часы: $selectedHour")
                Slider(
                    value = selectedHour.toFloat(),
                    onValueChange = { selectedHour = it.toInt() },
                    valueRange = 0f..23f,
                    steps = 22
                )
                
                Text("Минуты: $selectedMinute")
                Slider(
                    value = selectedMinute.toFloat(),
                    onValueChange = { selectedMinute = it.toInt() },
                    valueRange = 0f..59f,
                    steps = 58
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val now = LocalDateTime.now()
                    val time = now.withHour(selectedHour).withMinute(selectedMinute)
                    onTimeSelected(time)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
} 