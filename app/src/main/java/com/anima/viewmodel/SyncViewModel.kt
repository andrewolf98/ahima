package com.anima.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.sync.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SyncViewModel(application: Application) : AndroidViewModel(application) {
    private val syncRepository = SyncRepository(application)

    val syncStatus = syncRepository.syncStatus
    val storagePreferences = syncRepository.storagePreferences

    private val _syncInProgress = MutableStateFlow(false)
    val syncInProgress: StateFlow<Boolean> = _syncInProgress.asStateFlow()

    private val _lastSyncResult = MutableStateFlow<SyncResult?>(null)
    val lastSyncResult: StateFlow<SyncResult?> = _lastSyncResult.asStateFlow()

    fun updateStoragePreferences(useCloud: Boolean, provider: CloudProvider? = null) {
        val currentPrefs = storagePreferences.value
        syncRepository.updateStoragePreferences(
            currentPrefs.copy(
                useCloudStorage = useCloud,
                cloudProvider = provider
            )
        )
    }

    fun syncNow() {
        viewModelScope.launch {
            _syncInProgress.value = true
            try {
                val result = syncRepository.syncData()
                _lastSyncResult.value = result
            } finally {
                _syncInProgress.value = false
            }
        }
    }

    fun backupData() {
        viewModelScope.launch {
            _syncInProgress.value = true
            try {
                syncRepository.backupData()
            } finally {
                _syncInProgress.value = false
            }
        }
    }

    fun restoreData() {
        viewModelScope.launch {
            _syncInProgress.value = true
            try {
                syncRepository.restoreData()
            } finally {
                _syncInProgress.value = false
            }
        }
    }

    fun checkConnectivity() {
        syncRepository.checkConnectivity()
    }
} 