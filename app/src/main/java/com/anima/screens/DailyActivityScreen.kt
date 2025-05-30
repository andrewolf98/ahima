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
import androidx.compose.foundation.shape.RoundedCornerShape
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyActivityScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Задания", "Цели", "Награды")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Верхняя панель с прогрессом
        DailyProgressCard()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Табы
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
            0 -> ActivitiesContent()
            1 -> GoalsContent()
            2 -> RewardsContent()
        }
    }
}

@Composable
fun DailyProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Сегодняшний прогресс",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProgressItem(
                    icon = Icons.Default.Star,
                    value = "150",
                    label = "Очки"
                )
                ProgressItem(
                    icon = Icons.Default.TrendingUp,
                    value = "5",
                    label = "Дней подряд"
                )
                ProgressItem(
                    icon = Icons.Default.EmojiEvents,
                    value = "3",
                    label = "Уровень"
                )
            }
        }
    }
}

@Composable
fun ProgressItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ActivitiesContent() {
    val activities = remember { createMockActivities() }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(activities) { activity ->
            ActivityCard(activity = activity)
        }
    }
}

@Composable
fun ActivityCard(activity: DailyActivity) {
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
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "+${activity.points} очков",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (activity.category) {
                            ActivityCategory.COGNITIVE -> Icons.Default.Psychology
                            ActivityCategory.BEHAVIORAL -> Icons.Default.DirectionsRun
                            ActivityCategory.MEDITATIVE -> Icons.Default.SelfImprovement
                            ActivityCategory.SOCIAL -> Icons.Default.Group
                        },
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${activity.duration} мин",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                if (activity.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Выполнено",
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Button(
                        onClick = { /* TODO: Отметить как выполненное */ },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Выполнить")
                    }
                }
            }
        }
    }
}

@Composable
fun GoalsContent() {
    val goals = remember { createMockGoals() }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Цели на сегодня",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        items(goals.activities) { activity ->
            GoalCard(activity = activity)
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Советы",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
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
                    Text(
                        text = "Начните день с медитации",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Это поможет настроиться на продуктивный день и снизить уровень стресса",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun GoalCard(activity: DailyActivity) {
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
                imageVector = when (activity.category) {
                    ActivityCategory.COGNITIVE -> Icons.Default.Psychology
                    ActivityCategory.BEHAVIORAL -> Icons.Default.DirectionsRun
                    ActivityCategory.MEDITATIVE -> Icons.Default.SelfImprovement
                    ActivityCategory.SOCIAL -> Icons.Default.Group
                },
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (activity.isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Выполнено",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RewardsContent() {
    val rewards = remember { createMockRewards() }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Доступные награды",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        items(rewards) { reward ->
            RewardCard(reward = reward)
        }
    }
}

@Composable
fun RewardCard(reward: Reward) {
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
                Text(
                    text = reward.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${reward.pointsRequired} очков",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = reward.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (reward.isUnlocked) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Награда получена!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Вспомогательные функции для создания тестовых данных
private fun createMockActivities(): List<DailyActivity> {
    return listOf(
        DailyActivity(
            id = "1",
            title = "Утренняя медитация",
            description = "10 минут медитации для настройки на день",
            category = ActivityCategory.MEDITATIVE,
            difficulty = ActivityDifficulty.EASY,
            duration = 10,
            points = 50,
            tips = listOf("Найдите тихое место", "Сядьте удобно", "Следите за дыханием")
        ),
        DailyActivity(
            id = "2",
            title = "Когнитивное упражнение",
            description = "Запишите 3 позитивные мысли",
            category = ActivityCategory.COGNITIVE,
            difficulty = ActivityDifficulty.EASY,
            duration = 5,
            points = 30
        ),
        DailyActivity(
            id = "3",
            title = "Социальная активность",
            description = "Поделитесь своими достижениями с другом",
            category = ActivityCategory.SOCIAL,
            difficulty = ActivityDifficulty.MEDIUM,
            duration = 15,
            points = 40
        )
    )
}

private fun createMockGoals(): DailyGoals {
    return DailyGoals(
        date = LocalDateTime.now(),
        activities = createMockActivities(),
        totalPoints = 120,
        completedPoints = 50,
        streak = 5
    )
}

private fun createMockRewards(): List<Reward> {
    return listOf(
        Reward(
            id = "1",
            title = "Бронзовый уровень",
            description = "Достигните 100 очков",
            pointsRequired = 100,
            isUnlocked = true
        ),
        Reward(
            id = "2",
            title = "Серебряный уровень",
            description = "Достигните 500 очков",
            pointsRequired = 500
        ),
        Reward(
            id = "3",
            title = "Золотой уровень",
            description = "Достигните 1000 очков",
            pointsRequired = 1000
        )
    )
} 