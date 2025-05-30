package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.viewmodel.RelaxScreenViewModel
import com.anima.data.freesound.FreesoundSound
import com.anima.data.Result
import com.anima.data.freesound.FreesoundApiClient
import com.anima.data.freesound.FreesoundRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelaxScreen(viewModel: RelaxScreenViewModel = viewModel(factory = RelaxScreenViewModelFactory(FreesoundRepository(FreesoundApiClient.apiService)))) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Медитации", "Звуки", "Дыхание", "Микс", "Рекомендации")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> MeditationsContent()
            1 -> SoundsContent(viewModel = viewModel)
            2 -> BreathingContent()
            3 -> MixContent()
            4 -> RecommendationsContent()
        }
    }
}

@Composable
fun MeditationsContent() {
    val meditations = remember {
        listOf(
            "Утренняя медитация (10 мин)",
            "Снятие стресса (15 мин)",
            "Глубокий сон (20 мин)",
            "Концентрация (10 мин)",
            "Антистресс (15 мин)"
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(meditations) { meditation ->
            MeditationCard(title = meditation)
        }
    }
}

@Composable
fun MeditationCard(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SelfImprovement,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* TODO: Воспроизвести медитацию */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Воспроизвести")
            }
        }
    }
}

@Composable
fun SoundsContent(viewModel: RelaxScreenViewModel) {
    val soundsResult by viewModel.sounds.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (soundsResult) {
            is Result.Loading -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
            is Result.Success -> {
                val sounds = (soundsResult as Result.Success<List<FreesoundSound>>).data
                items(sounds) { sound ->
                    SoundCard(sound = sound)
                }
            }
            is Result.Error -> {
                item {
                    Text(
                        text = "Ошибка загрузки звуков: ${(soundsResult as Result.Error).exception.message}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun SoundCard(sound: FreesoundSound) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sound.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = sound.username,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = { /* TODO: Воспроизвести звук из sound.previews */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Воспроизвести")
            }
        }
    }
}

@Composable
fun BreathingContent() {
    val techniques = remember {
        listOf(
            "Квадратное дыхание",
            "4-7-8 дыхание",
            "Диафрагмальное дыхание",
            "Альтернативное дыхание"
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(techniques) { technique ->
            BreathingCard(title = technique)
        }
    }
}

@Composable
fun BreathingCard(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Air,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* TODO: Начать упражнение */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Начать")
            }
        }
    }
}

@Composable
fun MixContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Создайте свой микс",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // TODO: Добавить элементы для выбора медитаций, звуков и дыхательных техник
        // TODO: Добавить ползунки громкости для каждого элемента
        // TODO: Добавить кнопку воспроизведения микса

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Выберите компоненты микса:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Пример: выбор медитации
                Text("Медитация: Утренняя медитация") // TODO: Реализовать выбор
                Spacer(modifier = Modifier.height(4.dp))
                Text("Звук: Дождь") // TODO: Реализовать выбор
                Spacer(modifier = Modifier.height(4.dp))
                Text("Дыхание: Квадратное дыхание") // TODO: Реализовать выбор
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Воспроизвести микс */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Воспроизвести микс")
        }
    }
}

@Composable
fun RecommendationsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Рекомендации",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // TODO: Реализовать логику рекомендаций на основе времени суток и психоаналитики
        // TODO: Отобразить рекомендованный контент (медитации, звуки, дыхание, миксы)

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Рекомендуется для вас",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Медитация: Снятие стресса", style = MaterialTheme.typography.bodyMedium) // Пример рекомендации
                Text("Звук: Океан", style = MaterialTheme.typography.bodyMedium) // Пример рекомендации
            }
        }
    }
}

// Factory for RelaxScreenViewModel (для использования в Compose)
import androidx.lifecycle.ViewModelProvider

class RelaxScreenViewModelFactory(private val freesoundRepository: FreesoundRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RelaxScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RelaxScreenViewModel(freesoundRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 
} 