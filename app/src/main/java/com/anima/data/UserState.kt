package com.anima.data

import java.time.LocalDateTime

data class UserState(
    val id: String,
    val name: String,
    val age: Int,
    val country: String,
    val psychologicalProfile: PsychologicalProfile,
    val dailyProgress: DailyProgress,
    val settings: UserSettings
)

data class PsychologicalProfile(
    val phq9Score: Int, // Шкала депрессии
    val gad7Score: Int, // Шкала тревожности
    val lastTestDate: LocalDateTime,
    val diagnosis: String?,
    val riskFactors: List<RiskFactor>
)

data class RiskFactor(
    val type: RiskType,
    val level: RiskLevel,
    val description: String
)

enum class RiskType {
    ANXIETY,
    DEPRESSION,
    SLEEP_DISORDER,
    SOCIAL_ISOLATION,
    ADDICTION
}

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH
}

data class DailyProgress(
    val date: LocalDateTime,
    val completedTasks: List<CompletedTask>,
    val mood: Mood,
    val anxiety: Int, // 0-10
    val productivity: Int, // 0-10
    val meditationMinutes: Int,
    val sleepHours: Float
)

data class CompletedTask(
    val id: String,
    val type: TaskType,
    val completedAt: LocalDateTime,
    val duration: Int, // в минутах
    val rating: Int? // 1-5
)

enum class TaskType {
    MEDITATION,
    BREATHING,
    COGNITIVE,
    SOCIAL,
    PHYSICAL
}

enum class Mood {
    VERY_BAD,
    BAD,
    NEUTRAL,
    GOOD,
    VERY_GOOD
}

data class UserSettings(
    val notificationsEnabled: Boolean,
    val darkTheme: Boolean,
    val language: String,
    val syncEnabled: Boolean,
    val analyticsEnabled: Boolean,
    val soundVolume: Float,
    val autoPlay: Boolean,
    val backgroundPlay: Boolean
) 