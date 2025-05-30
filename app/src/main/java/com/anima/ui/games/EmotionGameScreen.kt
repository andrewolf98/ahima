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
import com.anima.games.monitoring.EmotionGame
import com.anima.data.monitoring.Emotion

@Composable
fun EmotionGameScreen(
    game: EmotionGame,
    onGameComplete: () -> Unit
) {
    val state = game.state
    var showTriggerDialog by remember { mutableStateOf(false) }
    var showStrategyDialog by remember { mutableStateOf(false) }
    
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
                text = "Дневник эмоций",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Выбор эмоции
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Выберите эмоцию",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Emotion.values().forEach { emotion ->
                            FilterChip(
                                selected = state.selectedEmotion == emotion,
                                onClick = { game.selectEmotion(emotion) },
                                label = { Text(emotion.name) }
                            )
                        }
                    }
                }
            }
            
            // Интенсивность
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Интенсивность: ${state.intensity}/10",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Slider(
                        value = state.intensity.toFloat(),
                        onValueChange = { game.setIntensity(it.toInt()) },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
            
            // Триггеры
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Триггеры",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    state.triggers.forEach { trigger ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(trigger)
                            IconButton(onClick = { game.removeTrigger(trigger) }) {
                                Text("×")
                            }
                        }
                    }
                    
                    Button(
                        onClick = { showTriggerDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Добавить триггер")
                    }
                }
            }
            
            // Стратегии
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Стратегии совладания",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    state.copingStrategies.forEach { strategy ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(strategy)
                            IconButton(onClick = { game.removeCopingStrategy(strategy) }) {
                                Text("×")
                            }
                        }
                    }
                    
                    Button(
                        onClick = { showStrategyDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Добавить стратегию")
                    }
                }
            }
            
            // Эффективность
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Эффективность стратегий: ${state.effectiveness}/10",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Slider(
                        value = state.effectiveness.toFloat(),
                        onValueChange = { game.setEffectiveness(it.toInt()) },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
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
                enabled = state.selectedEmotion != null && !state.isSaved
            ) {
                Text("Сохранить")
            }
        }
    }
    
    // Диалог добавления триггера
    if (showTriggerDialog) {
        var triggerText by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showTriggerDialog = false },
            title = { Text("Добавить триггер") },
            text = {
                Column {
                    TextField(
                        value = triggerText,
                        onValueChange = { triggerText = it },
                        label = { Text("Триггер") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        text = "Частые триггеры:",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    
                    game.getCommonTriggers().forEach { trigger ->
                        TextButton(
                            onClick = { 
                                triggerText = trigger
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(trigger)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (triggerText.isNotBlank()) {
                            game.addTrigger(triggerText)
                            showTriggerDialog = false
                        }
                    }
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTriggerDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
    
    // Диалог добавления стратегии
    if (showStrategyDialog) {
        var strategyText by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showStrategyDialog = false },
            title = { Text("Добавить стратегию") },
            text = {
                Column {
                    TextField(
                        value = strategyText,
                        onValueChange = { strategyText = it },
                        label = { Text("Стратегия") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        text = "Частые стратегии:",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    
                    game.getCommonStrategies().forEach { strategy ->
                        TextButton(
                            onClick = { 
                                strategyText = strategy
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(strategy)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (strategyText.isNotBlank()) {
                            game.addCopingStrategy(strategyText)
                            showStrategyDialog = false
                        }
                    }
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStrategyDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
} 