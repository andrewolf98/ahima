package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anima.data.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Основные", "Уведомления", "Синхронизация", "Безопасность")

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
            0 -> GeneralSettingsContent()
            1 -> NotificationSettingsContent()
            2 -> SyncSettingsContent()
            3 -> SecuritySettingsContent()
        }
    }
}

@Composable
fun GeneralSettingsContent() {
    var selectedLanguage by remember { mutableStateOf("Русский") }
    var darkMode by remember { mutableStateOf(false) }
    var soundEnabled by remember { mutableStateOf(true) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SettingsCard(
                title = "Язык",
                content = {
                    LanguageSelector(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { selectedLanguage = it }
                    )
                }
            )
        }
        
        item {
            SettingsCard(
                title = "Внешний вид",
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Тёмная тема")
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { darkMode = it }
                        )
                    }
                }
            )
        }
        
        item {
            SettingsCard(
                title = "Звуки",
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Звуковые эффекты")
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { soundEnabled = it }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun NotificationSettingsContent() {
    var meditationReminders by remember { mutableStateOf(true) }
    var dailyTasks by remember { mutableStateOf(true) }
    var progressUpdates by remember { mutableStateOf(false) }
    var emergencyAlerts by remember { mutableStateOf(true) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SettingsCard(
                title = "Уведомления",
                content = {
                    Column {
                        NotificationToggle(
                            title = "Напоминания о медитации",
                            checked = meditationReminders,
                            onCheckedChange = { meditationReminders = it }
                        )
                        NotificationToggle(
                            title = "Ежедневные задания",
                            checked = dailyTasks,
                            onCheckedChange = { dailyTasks = it }
                        )
                        NotificationToggle(
                            title = "Обновления прогресса",
                            checked = progressUpdates,
                            onCheckedChange = { progressUpdates = it }
                        )
                        NotificationToggle(
                            title = "Экстренные уведомления",
                            checked = emergencyAlerts,
                            onCheckedChange = { emergencyAlerts = it }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SyncSettingsContent() {
    var autoSync by remember { mutableStateOf(true) }
    var syncOnWifiOnly by remember { mutableStateOf(true) }
    var lastSyncTime by remember { mutableStateOf("Сегодня, 15:30") }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SettingsCard(
                title = "Синхронизация",
                content = {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Автоматическая синхронизация")
                            Switch(
                                checked = autoSync,
                                onCheckedChange = { autoSync = it }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Только по Wi-Fi")
                            Switch(
                                checked = syncOnWifiOnly,
                                onCheckedChange = { syncOnWifiOnly = it }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Последняя синхронизация: $lastSyncTime",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { /* TODO: Синхронизировать сейчас */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Синхронизировать сейчас")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SecuritySettingsContent() {
    var biometricAuth by remember { mutableStateOf(false) }
    var pinEnabled by remember { mutableStateOf(true) }
    var autoLock by remember { mutableStateOf(true) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SettingsCard(
                title = "Безопасность",
                content = {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Биометрическая аутентификация")
                            Switch(
                                checked = biometricAuth,
                                onCheckedChange = { biometricAuth = it }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("PIN-код")
                            Switch(
                                checked = pinEnabled,
                                onCheckedChange = { pinEnabled = it }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Автоблокировка")
                            Switch(
                                checked = autoLock,
                                onCheckedChange = { autoLock = it }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { /* TODO: Изменить PIN-код */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Изменить PIN-код")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    content: @Composable () -> Unit
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf("Русский", "English", "Español", "Deutsch", "Français")
    
    Column {
        languages.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(language)
                RadioButton(
                    selected = language == selectedLanguage,
                    onClick = { onLanguageSelected(language) }
                )
            }
        }
    }
}

@Composable
fun NotificationToggle(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
} 