package com.anima.data

import java.time.LocalDateTime

enum class ActivityCategory {
    COGNITIVE,    // Когнитивные упражнения
    BEHAVIORAL,   // Поведенческие практики
    MEDITATIVE,   // Медитативные практики
    SOCIAL        // Социальные активности
}

enum class ActivityDifficulty {
    EASY,
    MEDIUM,
    HARD
}

data class DailyActivity(
    val id: String,
    val title: String,
    val description: String,
    val category: ActivityCategory,
    val difficulty: ActivityDifficulty,
    val duration: Int, // в минутах
    val points: Int,
    val isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null,
    val tips: List<String> = emptyList()
)

data class DailyGoals(
    val date: LocalDateTime,
    val activities: List<DailyActivity>,
    val totalPoints: Int,
    val completedPoints: Int,
    val streak: Int // количество дней подряд выполнения заданий
)

data class UserProfile(
    val id: String,
    val name: String,
    val preferences: ActivityPreferences,
    val completedActivities: List<DailyActivity>,
    val totalPoints: Int,
    val level: Int,
    val streak: Int
)

data class ActivityPreferences(
    val preferredCategories: List<ActivityCategory>,
    val preferredDifficulty: ActivityDifficulty,
    val preferredDuration: Int, // в минутах
    val preferredTimeOfDay: String // "morning", "afternoon", "evening"
)

data class Reward(
    val id: String,
    val title: String,
    val description: String,
    val pointsRequired: Int,
    val isUnlocked: Boolean = false
) 