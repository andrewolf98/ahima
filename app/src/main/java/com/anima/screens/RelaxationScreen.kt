package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelaxationScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Медитации", "Звуки", "Дыхание", "Миксы")

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
            1 -> SoundsContent()
            2 -> BreathingContent()
            3 -> MixModesContent()
        }
    }
}

@Composable
private fun MeditationsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(createMockMeditations()) { meditation ->
            MeditationCard(meditation = meditation)
        }
    }
}

@Composable
private fun SoundsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(createMockSounds()) { sound ->
            SoundCard(sound = sound)
        }
    }
}

@Composable
private fun BreathingContent() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(createMockBreathingExercises()) { exercise ->
            BreathingCard(exercise = exercise)
        }
    }
}

@Composable
private fun MixModesContent() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(createMockMixModes()) { mix ->
            MixModeCard(mix = mix)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationCard(meditation: Meditation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Открыть медитацию */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = meditation.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    imageVector = Icons.Default.SelfImprovement,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = meditation.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${meditation.duration} мин",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = { /* TODO: Начать медитацию */ },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Начать")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundCard(sound: Sound) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Открыть звук */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sound.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = sound.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${sound.duration} мин",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = { /* TODO: Воспроизвести звук */ },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Воспроизвести")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingCard(exercise: BreathingExercise) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Открыть упражнение */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${exercise.cycles} циклов",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = { /* TODO: Начать упражнение */ },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Начать")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MixModeCard(mix: MixMode) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Открыть микс */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mix.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    imageVector = Icons.Default.Mix,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = mix.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${mix.duration} мин",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = { /* TODO: Начать микс */ },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Начать")
                }
            }
        }
    }
}

private fun createMockMeditations(): List<Meditation> {
    return listOf(
        Meditation(
            id = "1",
            title = "Утренняя медитация",
            description = "Начните день с осознанности",
            duration = 10,
            audioUrl = "meditation_morning.mp3",
            category = "Утро"
        ),
        Meditation(
            id = "2",
            title = "Снятие стресса",
            description = "Избавьтесь от напряжения",
            duration = 15,
            audioUrl = "meditation_stress.mp3",
            category = "Стресс"
        )
    )
}

private fun createMockSounds(): List<Sound> {
    return listOf(
        Sound(
            id = "1",
            title = "Дождь",
            description = "Звуки дождя",
            duration = 60,
            audioUrl = "sound_rain.mp3",
            category = "Природа"
        ),
        Sound(
            id = "2",
            title = "Океан",
            description = "Звуки океана",
            duration = 60,
            audioUrl = "sound_ocean.mp3",
            category = "Природа"
        )
    )
}

private fun createMockBreathingExercises(): List<BreathingExercise> {
    return listOf(
        BreathingExercise(
            id = "1",
            title = "4-7-8",
            description = "Техника дыхания для снятия стресса",
            inhaleDuration = 4,
            holdDuration = 7,
            exhaleDuration = 8,
            cycles = 4
        ),
        BreathingExercise(
            id = "2",
            title = "Квадратное дыхание",
            description = "Техника для концентрации",
            inhaleDuration = 4,
            holdDuration = 4,
            exhaleDuration = 4,
            cycles = 5
        )
    )
}

private fun createMockMixModes(): List<MixMode> {
    return listOf(
        MixMode(
            id = "1",
            title = "Сон",
            description = "Комбинация для улучшения сна",
            meditationId = "2",
            soundId = "1",
            duration = 30
        ),
        MixMode(
            id = "2",
            title = "Концентрация",
            description = "Комбинация для улучшения концентрации",
            meditationId = "1",
            soundId = "2",
            duration = 20
        )
    )
} 