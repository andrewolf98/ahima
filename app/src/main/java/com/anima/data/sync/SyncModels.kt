package com.anima.data.sync

import java.util.Date

data class SyncStatus(
    val isOnline: Boolean,
    val lastSyncTime: Date?,
    val syncError: String? = null
)

data class StoragePreferences(
    val useCloudStorage: Boolean,
    val cloudProvider: CloudProvider? = null,
    val autoSync: Boolean = true,
    val syncInterval: Int = 15 // minutes
)

enum class CloudProvider {
    GOOGLE_DRIVE,
    DROPBOX,
    ONE_DRIVE
}

data class SyncMetadata(
    val deviceId: String,
    val lastModified: Date,
    val version: Int,
    val dataType: String
)

data class SyncResult(
    val success: Boolean,
    val error: String? = null,
    val itemsSynced: Int = 0,
    val itemsFailed: Int = 0
) 