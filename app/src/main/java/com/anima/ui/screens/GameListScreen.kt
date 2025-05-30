package com.anima.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.diagnostic.ТипИгры

@Composable
fun GameListScreen(
    onGameSelected: (ТипИгры) -> Unit
) {
    val games = listOf(
        GameItem(
            title = "Эмоциональный сортер",
            description = "Сортировка эмоций по категориям",
            type = ТипИгры.ЭМОЦИОНАЛЬНЫЙ_СОРТЕР
        ),
        GameItem(
            title = "Тест Струпа",
            description = "Определение цвета текста",
            type = ТипИгры.СТРУП_ТЕСТ
        ),
        GameItem(
            title = "Память чисел",
            description = "Запоминание последовательности чисел",
            type = ТипИгры.ПАМЯТЬ_ЧИСЕЛ
        ),
        GameItem(
            title = "Поиск отличий",
            description = "Найдите различия между изображениями",
            type = ТипИгры.ПОИСК_ОТЛИЧИЙ
        ),
        GameItem(
            title = "Ассоциации слов",
            description = "Подберите связанные слова",
            type = ТипИгры.АССОЦИАЦИИ_СЛОВ
        ),
        GameItem(
            title = "Время реакции",
            description = "Измерьте скорость вашей реакции",
            type = ТипИгры.ВРЕМЯ_РЕАКЦИИ
        ),
        GameItem(
            title = "Сортировка карточек",
            description = "Сортировка карточек по правилам",
            type = ТипИгры.СОРТИРОВКА_КАРТОЧЕК
        ),
        GameItem(
            title = "Распознавание интонаций",
            description = "Определите эмоцию по интонации",
            type = ТипИгры.РАСПОЗНАВАНИЕ_ИНТОНАЦИЙ
        ),
        GameItem(
            title = "Пространственный тест",
            description = "Тест на пространственное мышление",
            type = ТипИгры.ПРОСТРАНСТВЕННЫЙ_ТЕСТ
        ),
        GameItem(
            title = "Flanker Task",
            description = "Тест на концентрацию внимания",
            type = ТипИгры.FLANKER_TASK
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Диагностические игры",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(games) { game ->
                GameCard(
                    game = game,
                    onClick = { onGameSelected(game.type) }
                )
            }
        }
    }
}

@Composable
fun GameCard(
    game: GameItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = game.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class GameItem(
    val title: String,
    val description: String,
    val type: ТипИгры
) 