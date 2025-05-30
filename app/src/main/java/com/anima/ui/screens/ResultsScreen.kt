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
import com.anima.viewmodel.DiagnosticGamesViewModel

@Composable
fun ResultsScreen(
    onBack: () -> Unit,
    onNewGame: () -> Unit,
    viewModel: DiagnosticGamesViewModel = viewModel()
) {
    val results = viewModel.getGameResults()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Результаты диагностики",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results) { result ->
                ResultCard(result = result)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onNewGame,
                modifier = Modifier.weight(1f)
            ) {
                Text("Новая игра")
            }
        }
    }
}

@Composable
fun ResultCard(result: GameResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = when (result.gameType) {
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
                },
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Правильных ответов: ${result.score} из ${result.total}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "Время: ${result.timeSpent / 1000} сек",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "Ошибок: ${result.mistakes}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 