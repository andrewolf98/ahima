package com.anima.data.model

data class UserSettings(
    val userId: String,
    val notificationsEnabled: Boolean,
    val darkMode: Boolean,
    val language: String,
    val syncEnabled: Boolean,
    val reminderTime: String,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean
) 