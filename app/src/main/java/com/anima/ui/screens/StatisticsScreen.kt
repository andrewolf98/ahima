package com.anima.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.data.diagnostic.ТипИгры
import com.anima.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    viewModel: StatisticsViewModel = viewModel()
) {
    val statistics by viewModel.statistics.collectAsState()
    val selectedGameType by viewModel.selectedGameType.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Статистика",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Выбор типа игры
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = when (selectedGameType) {
                    ТипИгры.ЭМОЦИОНАЛЬНЫЙ_СОРТЕР -> "Эмоциональный сортер"
                    ТипИгры.СТРУП_ТЕСТ -> "Тест Струпа"
                    ТипИгры.ПАМЯТЬ_ЧИСЕЛ -> "Память чисел"
                    ТипИгры.ПОИСК_ОТЛИЧИЙ -> "Поиск отличий"
                    ТипИгры.АССОЦИАЦИИ_СЛОВ -> "Ассоциации слов"
                    ТипИгры.ВРЕМЯ_РЕАКЦИИ -> "Время реакции"
                    ТипИгры.СОРТИРОВКА_КАРТОЧЕК -> "Сортировка карточек"
                    ТипИгры.РАСПОЗНАВАНИЕ_ИНТОНАЦИЙ -> "Распознавание интонаций"
                    ТипИгры.ПРОСТРАНСТВЕННЫЙ_ТЕСТ -> "Пространственный тест"
                    ТипИгры.FLANKER_TASK -> "Flanker Task"
                    null -> "Все игры"
                },
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = false,
                onDismissRequest = { }
            ) {
                ТипИгры.values().forEach { gameType ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                when (gameType) {
                                    ТипИгры.ЭМОЦИОНАЛЬНЫЙ_СОРТЕР -> "Эмоциональный сортер"
                                    ТипИгры.СТРУП_ТЕСТ -> "Тест Струпа"
                                    ТипИгры.ПАМЯТЬ_ЧИСЕЛ -> "Память чисел"
                                    ТипИгры.ПОИСК_ОТЛИЧИЙ -> "Поиск отличий"
                                    ТипИгры.АССОЦИАЦИИ_СЛОВ -> "Ассоциации слов"
                                    ТипИгры.ВРЕМЯ_РЕАКЦИИ -> "Время реакции"
                                    ТипИгры.СОРТИРОВКА_КАРТОЧЕК -> "Сортировка карточек"
                                    ТипИгры.РАСПОЗНАВАНИЕ_ИНТОНАЦИЙ -> "Распознавание интонаций"
                                    ТипИгры.ПРОСТРАНСТВЕННЫЙ_ТЕСТ -> "Пространственный тест"
                                    ТипИгры.FLANKER_TASK -> "Flanker Task"
                                }
                            )
                        },
                        onClick = { viewModel.selectGameType(gameType) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Статистика
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                StatisticCard(
                    title = "Всего игр",
                    value = statistics.totalGames.toString()
                )
            }
            
            item {
                StatisticCard(
                    title = "Средний результат",
                    value = String.format("%.1f%%", statistics.averageScore * 100)
                )
            }
            
            item {
                StatisticCard(
                    title = "Лучший результат",
                    value = "${statistics.bestScore}%"
                )
            }
            
            item {
                StatisticCard(
                    title = "Среднее время",
                    value = "${statistics.averageTime / 1000} сек"
                )
            }
            
            item {
                StatisticCard(
                    title = "Всего ошибок",
                    value = statistics.totalMistakes.toString()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}

@Composable
fun StatisticCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
} 