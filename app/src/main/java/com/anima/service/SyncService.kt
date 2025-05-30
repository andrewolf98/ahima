package com.anima.service

import android.content.Context
import com.anima.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class SyncService(private val context: Context) {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _lastSyncTime = MutableStateFlow<LocalDateTime?>(null)
    val lastSyncTime: StateFlow<LocalDateTime?> = _lastSyncTime.asStateFlow()

    private val _syncError = MutableStateFlow<String?>(null)
    val syncError: StateFlow<String?> = _syncError.asStateFlow()

    suspend fun syncUserData(userState: UserState) {
        try {
            _syncState.value = SyncState.Syncing
            // TODO: Реализовать синхронизацию с сервером
            // 1. Отправка данных на сервер
            // 2. Получение обновлений с сервера
            // 3. Обновление локальных данных
            _lastSyncTime.value = LocalDateTime.now()
            _syncState.value = SyncState.Success
        } catch (e: Exception) {
            _syncError.value = e.message
            _syncState.value = SyncState.Error
        }
    }

    suspend fun syncProgress(progress: DailyProgress) {
        try {
            _syncState.value = SyncState.Syncing
            // TODO: Реализовать синхронизацию прогресса с сервером
            _lastSyncTime.value = LocalDateTime.now()
            _syncState.value = SyncState.Success
        } catch (e: Exception) {
            _syncError.value = e.message
            _syncState.value = SyncState.Error
        }
    }

    suspend fun syncTestResults(testResult: TestResult) {
        try {
            _syncState.value = SyncState.Syncing
            // TODO: Реализовать синхронизацию результатов тестов с сервером
            _lastSyncTime.value = LocalDateTime.now()
            _syncState.value = SyncState.Success
        } catch (e: Exception) {
            _syncError.value = e.message
            _syncState.value = SyncState.Error
        }
    }

    suspend fun syncAchievements(achievements: List<Achievement>) {
        try {
            _syncState.value = SyncState.Syncing
            // TODO: Реализовать синхронизацию достижений с сервером
            _lastSyncTime.value = LocalDateTime.now()
            _syncState.value = SyncState.Success
        } catch (e: Exception) {
            _syncError.value = e.message
            _syncState.value = SyncState.Error
        }
    }

    fun shouldSync(): Boolean {
        val lastSync = _lastSyncTime.value ?: return true
        val hoursSinceLastSync = TimeUnit.HOURS.convert(
            ChronoUnit.MILLIS.between(lastSync, LocalDateTime.now()),
            TimeUnit.MILLISECONDS
        )
        return hoursSinceLastSync >= 24
    }

    fun clearSyncError() {
        _syncError.value = null
    }
}

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    object Success : SyncState()
    object Error : SyncState()
}

class SyncPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isAutoSyncEnabled: Boolean
        get() = prefs.getBoolean(KEY_AUTO_SYNC, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_SYNC, value).apply()

    var syncInterval: Int
        get() = prefs.getInt(KEY_SYNC_INTERVAL, 24)
        set(value) = prefs.edit().putInt(KEY_SYNC_INTERVAL, value).apply()

    var lastSyncTime: Long
        get() = prefs.getLong(KEY_LAST_SYNC, 0)
        set(value) = prefs.edit().putLong(KEY_LAST_SYNC, value).apply()

    companion object {
        private const val PREFS_NAME = "sync_preferences"
        private const val KEY_AUTO_SYNC = "auto_sync"
        private const val KEY_SYNC_INTERVAL = "sync_interval"
        private const val KEY_LAST_SYNC = "last_sync"
    }
}

class SyncWorker(
    context: Context,
    workerParams: androidx.work.WorkerParameters
) : androidx.work.CoroutineWorker(context, workerParams) {

    private val syncService = SyncService(context)
    private val syncPreferences = SyncPreferences(context)

    override suspend fun doWork(): Result {
        return try {
            if (syncPreferences.isAutoSyncEnabled) {
                // TODO: Реализовать автоматическую синхронизацию
                Result.success()
            } else {
                Result.success()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        fun scheduleSync(context: Context) {
            val syncPreferences = SyncPreferences(context)
            val syncInterval = syncPreferences.syncInterval

            val syncRequest = androidx.work.PeriodicWorkRequestBuilder<SyncWorker>(
                syncInterval.toLong(),
                TimeUnit.HOURS
            ).build()

            androidx.work.WorkManager.getInstance(context).enqueue(syncRequest)
        }
    }
} 