package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.viewmodel.SyncViewModel
import com.anima.data.sync.CloudProvider
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncSettingsScreen(viewModel: SyncViewModel = viewModel()) {
    val syncStatus by viewModel.syncStatus.collectAsState()
    val storagePreferences by viewModel.storagePreferences.collectAsState()
    val syncInProgress by viewModel.syncInProgress.collectAsState()
    val lastSyncResult by viewModel.lastSyncResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Online status
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (syncStatus.isOnline)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (syncStatus.isOnline) "Онлайн" else "Офлайн",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    onClick = { viewModel.checkConnectivity() },
                    enabled = !syncInProgress
                ) {
                    Text("Проверить")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Storage preferences
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Настройки хранения",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Использовать облачное хранилище")
                    Switch(
                        checked = storagePreferences.useCloudStorage,
                        onCheckedChange = { viewModel.updateStoragePreferences(it) }
                    )
                }

                if (storagePreferences.useCloudStorage) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Выберите облачное хранилище:")
                    CloudProviderSelector(
                        selectedProvider = storagePreferences.cloudProvider,
                        onProviderSelected = { viewModel.updateStoragePreferences(true, it) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sync status
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Статус синхронизации",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                syncStatus.lastSyncTime?.let { lastSync ->
                    Text(
                        text = "Последняя синхронизация: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(lastSync)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                syncStatus.syncError?.let { error ->
                    Text(
                        text = "Ошибка: $error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                lastSyncResult?.let { result ->
                    Text(
                        text = "Синхронизировано элементов: ${result.itemsSynced}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.syncNow() },
                        enabled = !syncInProgress && syncStatus.isOnline,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Синхронизировать сейчас")
                    }

                    Button(
                        onClick = { viewModel.backupData() },
                        enabled = !syncInProgress,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Создать резервную копию")
                    }
                }
            }
        }
    }
}

@Composable
fun CloudProviderSelector(
    selectedProvider: CloudProvider?,
    onProviderSelected: (CloudProvider) -> Unit
) {
    Column {
        CloudProvider.values().forEach { provider ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = provider == selectedProvider,
                    onClick = { onProviderSelected(provider) }
                )
                Text(
                    text = when (provider) {
                        CloudProvider.GOOGLE_DRIVE -> "Google Drive"
                        CloudProvider.DROPBOX -> "Dropbox"
                        CloudProvider.ONE_DRIVE -> "OneDrive"
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
} 