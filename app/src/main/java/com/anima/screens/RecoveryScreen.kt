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
import com.anima.data.DailyTask
import com.anima.data.TaskType
import java.time.LocalDateTime

@Composable
fun RecoveryScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Задания", "Тесты")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Верхняя панель с табами
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // Контент в зависимости от выбранного таба
        when (selectedTab) {
            0 -> DailyTasksContent()
            1 -> TestsContent()
        }
    }
}

@Composable
fun DailyTasksContent() {
    val tasks = remember {
        listOf(
            "Утренняя медитация",
            "Дыхательные упражнения",
            "Прогулка на свежем воздухе",
            "Ведение дневника",
            "Вечерняя релаксация"
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = task,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun TestsContent() {
    val tests = remember {
        listOf(
            "Тест на уровень стресса",
            "Оценка эмоционального состояния",
            "Тест на качество сна",
            "Оценка уровня тревожности"
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tests) { test ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = test,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(task: DailyTask) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Открыть детали задачи */ }
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
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    imageVector = when (task.type) {
                        TaskType.MEDITATION -> Icons.Default.SelfImprovement
                        TaskType.BREATHING -> Icons.Default.Air
                        TaskType.ACTIVITY -> Icons.Default.DirectionsRun
                        TaskType.COGNITIVE -> Icons.Default.Psychology
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = task.description,
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
                    text = "${task.duration} мин",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Выполнено",
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Button(
                        onClick = { /* TODO: Отметить задачу как выполненную */ },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Выполнить")
                    }
                }
            }
        }
    }
}

private fun createMockDailyTasks(): List<DailyTask> {
    return listOf(
        DailyTask(
            id = "1",
            title = "Утренняя медитация",
            description = "Начните день с 10-минутной медитации",
            type = TaskType.MEDITATION,
            duration = 10,
            isCompleted = true,
            rating = 4
        ),
        DailyTask(
            id = "2",
            title = "Дыхательные упражнения",
            description = "Выполните 5 минут дыхательных упражнений",
            type = TaskType.BREATHING,
            duration = 5,
            isCompleted = true,
            rating = 5
        ),
        DailyTask(
            id = "3",
            title = "Прогулка на свежем воздухе",
            description = "Совершите 30-минутную прогулку",
            type = TaskType.ACTIVITY,
            duration = 30,
            isCompleted = false,
            rating = 0
        ),
        DailyTask(
            id = "4",
            title = "Когнитивное упражнение",
            description = "Запишите 3 позитивные мысли",
            type = TaskType.COGNITIVE,
            duration = 15,
            isCompleted = false,
            rating = 0
        )
    )
} 