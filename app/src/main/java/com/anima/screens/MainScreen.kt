package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.anima.service.AuthService
import com.anima.service.AuthState

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Recovery : Screen("recovery", Icons.Default.Home, "Восстановление")
    object Relaxation : Screen("relaxation", Icons.Default.SelfImprovement, "Релаксация")
    object Profile : Screen("profile", Icons.Default.Person, "Профиль")
    object Analytics : Screen("analytics", Icons.Default.Analytics, "Аналитика")
    object Support : Screen("support", Icons.Default.Support, "Поддержка")
    object Settings : Screen("settings", Icons.Default.Settings, "Настройки")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authService: AuthService,
    onSignOut: () -> Unit
) {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Recovery) }
    val currentUser by authService.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedScreen.title) },
                actions = {
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                listOf(
                    Screen.Recovery,
                    Screen.Relaxation,
                    Screen.Profile,
                    Screen.Analytics,
                    Screen.Support,
                    Screen.Settings
                ).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedScreen) {
                Screen.Recovery -> RecoveryScreen()
                Screen.Relaxation -> RelaxationScreen()
                Screen.Profile -> ProfileScreen(currentUser)
                Screen.Analytics -> AnalyticsScreen()
                Screen.Support -> SupportScreen()
                Screen.Settings -> SettingsScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen(currentUser: UserState?) {
    if (currentUser == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Пользователь не найден")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Личная информация",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ProfileItem("Имя", currentUser.name)
                ProfileItem("Возраст", currentUser.age.toString())
                ProfileItem("Страна", currentUser.country)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Психологический профиль",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ProfileItem(
                    "Уровень депрессии",
                    "${currentUser.psychologicalProfile.depressionScore}/27"
                )
                ProfileItem(
                    "Уровень тревожности",
                    "${currentUser.psychologicalProfile.anxietyScore}/21"
                )
                ProfileItem(
                    "Последний тест",
                    currentUser.psychologicalProfile.lastTestDate?.toString() ?: "Не пройден"
                )
                if (currentUser.psychologicalProfile.diagnosis != null) {
                    ProfileItem(
                        "Диагноз",
                        currentUser.psychologicalProfile.diagnosis
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Ежедневный прогресс",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ProfileItem(
                    "Выполнено задач",
                    currentUser.dailyProgress.completedTasks.toString()
                )
                ProfileItem(
                    "Настроение",
                    currentUser.dailyProgress.mood.toString()
                )
                ProfileItem(
                    "Уровень тревожности",
                    "${currentUser.dailyProgress.anxietyLevel}/10"
                )
                ProfileItem(
                    "Продуктивность",
                    "${currentUser.dailyProgress.productivity}/10"
                )
                ProfileItem(
                    "Медитация",
                    "${currentUser.dailyProgress.meditationMinutes} мин"
                )
                ProfileItem(
                    "Сон",
                    "${currentUser.dailyProgress.sleepHours} ч"
                )
            }
        }
    }
}

@Composable
private fun ProfileItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 