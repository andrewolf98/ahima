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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.viewmodel.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Состояние", "Динамика", "Отчёты")

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
            0 -> StateContent(viewModel = viewModel)
            1 -> DynamicsContent(viewModel = viewModel)
            2 -> ReportsContent()
        }
    }
}

@Composable
fun StateContent(viewModel: AnalyticsViewModel) {
    val moodTrend by viewModel.moodTrend.collectAsState()
    val anxietyTrend by viewModel.anxietyTrend.collectAsState()
    val productivityTrend by viewModel.productivityTrend.collectAsState()
    val sleepQualityTrend by viewModel.sleepQualityTrend.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            StateCard(
                title = "Эмоциональное состояние",
                value = moodTrend?.value?.toInt()?.toString() ?: "Загрузка...", // TODO: Отображать более понятное значение состояния
                trend = moodTrend?.recommendations ?: "Загрузка тренда...",
                icon = Icons.Default.Mood
            )
        }
        item {
            StateCard(
                title = "Уровень тревожности",
                value = anxietyTrend?.value?.toInt()?.toString() ?: "Загрузка...", // TODO: Отображать более понятное значение уровня
                trend = anxietyTrend?.recommendations ?: "Загрузка тренда...",
                icon = Icons.Default.Speed
            )
        }
        item {
            StateCard(
                title = "Продуктивность",
                value = productivityTrend?.value?.toInt()?.toString() ?: "Загрузка...", // TODO: Отображать более понятное значение продуктивности
                trend = productivityTrend?.recommendations ?: "Загрузка тренда...",
                icon = Icons.Default.Assessment
            )
        }
        item {
            StateCard(
                title = "Качество сна",
                value = sleepQualityTrend?.value?.toInt()?.toString() ?: "Загрузка...", // TODO: Отображать более понятное значение качества сна
                trend = sleepQualityTrend?.recommendations ?: "Загрузка тренда...",
                icon = Icons.Default.Bedtime
            )
        }
    }
}

@Composable
fun StateCard(
    title: String,
    value: String,
    trend: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // TODO: Добавить график состояния
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Рекомендация: $trend",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DynamicsContent(viewModel: AnalyticsViewModel) {
    val progressHistory by viewModel.progressHistory.collectAsState()
    val meditationConsistency by viewModel.meditationConsistency.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Динамика прогресса",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                DynamicsCard(
                    title = "Регулярность медитаций",
                    period = "За всё время", // TODO: Добавить выбор периода
                    icon = Icons.Default.SelfImprovement
                )
                meditationConsistency?.let {
                    Text(
                        text = "Рекомендация: ${it.recommendations}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    )
                }
            }
        }

        // TODO: Добавить карточки динамики для других метрик (тревожность, продуктивность, сон)
        // TODO: Отобразить графики динамики
    }
}

@Composable
fun DynamicsCard(
    title: String,
    period: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = period,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // TODO: Добавить график динамики
        }
    }
}

@Composable
fun ReportsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Отчёты",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item { // Пример отчета
             ReportCard(
                title = "Еженедельный отчёт",
                date = "1-7 марта 2024",
                icon = Icons.Default.Assessment
            )
        }
        // TODO: Добавить другие типы отчетов
    }
}

@Composable
fun ReportCard(
    title: String,
    date: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
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
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* TODO: Открыть отчёт */ }) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Скачать отчёт"
                )
            }
        }
    }
}

private fun createMockProgressHistory(): List<DailyProgress> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return listOf(
        DailyProgress(
            date = LocalDate.parse("2024-03-01", formatter),
            mood = 4,
            anxiety = 3,
            productivity = 4,
            meditationMinutes = 15,
            tasksCompleted = 3
        ),
        DailyProgress(
            date = LocalDate.parse("2024-03-02", formatter),
            mood = 3,
            anxiety = 4,
            productivity = 3,
            meditationMinutes = 10,
            tasksCompleted = 2
        ),
        DailyProgress(
            date = LocalDate.parse("2024-03-03", formatter),
            mood = 5,
            anxiety = 2,
            productivity = 5,
            meditationMinutes = 20,
            tasksCompleted = 4
        )
    )
}

private fun calculateMoodTrend(progressHistory: List<DailyProgress>): Trend {
    val averageMood = progressHistory.map { it.mood }.average()
    val recommendations = when {
        averageMood < 3 -> "Рекомендуем больше времени уделять медитациям и дыхательным упражнениям"
        averageMood < 4 -> "Попробуйте добавить больше физической активности в ваш день"
        else -> "Продолжайте поддерживать хорошее настроение"
    }
    return Trend(
        value = averageMood,
        recommendations = recommendations
    )
}

private fun calculateAnxietyTrend(progressHistory: List<DailyProgress>): Trend {
    val averageAnxiety = progressHistory.map { it.anxiety }.average()
    val recommendations = when {
        averageAnxiety > 4 -> "Рекомендуем больше времени уделять медитациям и дыхательным упражнениям"
        averageAnxiety > 3 -> "Попробуйте добавить больше физической активности в ваш день"
        else -> "Продолжайте поддерживать низкий уровень тревоги"
    }
    return Trend(
        value = averageAnxiety,
        recommendations = recommendations
    )
}

private fun calculateProductivityScore(progressHistory: List<DailyProgress>): Trend {
    val averageProductivity = progressHistory.map { it.productivity }.average()
    val recommendations = when {
        averageProductivity < 3 -> "Рекомендуем больше времени уделять медитациям и дыхательным упражнениям"
        averageProductivity < 4 -> "Попробуйте добавить больше физической активности в ваш день"
        else -> "Продолжайте поддерживать высокую продуктивность"
    }
    return Trend(
        value = averageProductivity,
        recommendations = recommendations
    )
}

private fun calculateMeditationConsistency(progressHistory: List<DailyProgress>): Trend {
    val averageMeditationMinutes = progressHistory.map { it.meditationMinutes }.average()
    val recommendations = when {
        averageMeditationMinutes < 10 -> "Рекомендуем увеличить время медитаций до 15-20 минут"
        averageMeditationMinutes < 20 -> "Попробуйте добавить еще 5-10 минут к вашим медитациям"
        else -> "Продолжайте поддерживать регулярную практику медитаций"
    }
    return Trend(
        value = averageMeditationMinutes,
        recommendations = recommendations
    )
}

data class Trend(
    val value: Double,
    val recommendations: String
) 