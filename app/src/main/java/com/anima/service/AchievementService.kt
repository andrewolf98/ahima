package com.anima.service

import com.anima.data.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AchievementService {
    fun checkAchievements(userState: UserState, progressHistory: List<DailyProgress>): List<Achievement> {
        val achievements = mutableListOf<Achievement>()

        // Проверка достижений по медитации
        checkMeditationAchievements(userState, progressHistory)?.let { achievements.add(it) }

        // Проверка достижений по заданиям
        checkTaskAchievements(userState, progressHistory)?.let { achievements.add(it) }

        // Проверка достижений по тестам
        checkTestAchievements(userState)?.let { achievements.add(it) }

        // Проверка достижений по настроению
        checkMoodAchievements(progressHistory)?.let { achievements.add(it) }

        return achievements
    }

    private fun checkMeditationAchievements(
        userState: UserState,
        progressHistory: List<DailyProgress>
    ): Achievement? {
        val totalMeditationMinutes = progressHistory.sumOf { it.meditationMinutes }
        val meditationDays = progressHistory.count { it.meditationMinutes > 0 }
        val firstMeditationDate = progressHistory.firstOrNull { it.meditationMinutes > 0 }?.date
        val daysSinceFirstMeditation = firstMeditationDate?.let {
            ChronoUnit.DAYS.between(it, LocalDateTime.now())
        } ?: 0

        return when {
            totalMeditationMinutes >= 1000 -> {
                Achievement(
                    id = "med_1000",
                    title = "Мастер медитации",
                    description = "Накоплено 1000 минут медитации",
                    type = AchievementType.MEDITATION,
                    date = LocalDateTime.now()
                )
            }
            totalMeditationMinutes >= 500 -> {
                Achievement(
                    id = "med_500",
                    title = "Опытный практик",
                    description = "Накоплено 500 минут медитации",
                    type = AchievementType.MEDITATION,
                    date = LocalDateTime.now()
                )
            }
            meditationDays >= 30 -> {
                Achievement(
                    id = "med_30days",
                    title = "Месяц осознанности",
                    description = "30 дней практики медитации",
                    type = AchievementType.MEDITATION,
                    date = LocalDateTime.now()
                )
            }
            meditationDays >= 7 && daysSinceFirstMeditation <= 10 -> {
                Achievement(
                    id = "med_7days",
                    title = "Неделя практики",
                    description = "7 дней подряд практики медитации",
                    type = AchievementType.MEDITATION,
                    date = LocalDateTime.now()
                )
            }
            else -> null
        }
    }

    private fun checkTaskAchievements(
        userState: UserState,
        progressHistory: List<DailyProgress>
    ): Achievement? {
        val totalTasks = progressHistory.sumOf { it.completedTasks.size }
        val taskDays = progressHistory.count { it.completedTasks.isNotEmpty() }
        val firstTaskDate = progressHistory.firstOrNull { it.completedTasks.isNotEmpty() }?.date
        val daysSinceFirstTask = firstTaskDate?.let {
            ChronoUnit.DAYS.between(it, LocalDateTime.now())
        } ?: 0

        return when {
            totalTasks >= 100 -> {
                Achievement(
                    id = "task_100",
                    title = "Целеустремленный",
                    description = "Выполнено 100 заданий",
                    type = AchievementType.TASK,
                    date = LocalDateTime.now()
                )
            }
            totalTasks >= 50 -> {
                Achievement(
                    id = "task_50",
                    title = "Активный участник",
                    description = "Выполнено 50 заданий",
                    type = AchievementType.TASK,
                    date = LocalDateTime.now()
                )
            }
            taskDays >= 30 -> {
                Achievement(
                    id = "task_30days",
                    title = "Регулярность",
                    description = "30 дней выполнения заданий",
                    type = AchievementType.TASK,
                    date = LocalDateTime.now()
                )
            }
            taskDays >= 7 && daysSinceFirstTask <= 10 -> {
                Achievement(
                    id = "task_7days",
                    title = "Начало пути",
                    description = "7 дней подряд выполнения заданий",
                    type = AchievementType.TASK,
                    date = LocalDateTime.now()
                )
            }
            else -> null
        }
    }

    private fun checkTestAchievements(userState: UserState): Achievement? {
        val phq9Score = userState.psychologicalProfile.phq9Score
        val gad7Score = userState.psychologicalProfile.gad7Score
        val lastTestDate = userState.psychologicalProfile.lastTestDate
        val daysSinceLastTest = ChronoUnit.DAYS.between(lastTestDate, LocalDateTime.now())

        return when {
            phq9Score <= 4 && gad7Score <= 4 -> {
                Achievement(
                    id = "test_balanced",
                    title = "Эмоциональный баланс",
                    description = "Низкий уровень тревожности и депрессии",
                    type = AchievementType.TEST,
                    date = LocalDateTime.now()
                )
            }
            daysSinceLastTest <= 14 -> {
                Achievement(
                    id = "test_regular",
                    title = "Регулярный мониторинг",
                    description = "Регулярное прохождение тестов",
                    type = AchievementType.TEST,
                    date = LocalDateTime.now()
                )
            }
            else -> null
        }
    }

    private fun checkMoodAchievements(progressHistory: List<DailyProgress>): Achievement? {
        val recentProgress = progressHistory.takeLast(7)
        val positiveMoodDays = recentProgress.count { it.mood == Mood.HAPPY }
        val averageMood = recentProgress.map { it.mood.ordinal }.average()

        return when {
            positiveMoodDays >= 5 -> {
                Achievement(
                    id = "mood_positive",
                    title = "Позитивный настрой",
                    description = "5 дней с хорошим настроением",
                    type = AchievementType.MOOD,
                    date = LocalDateTime.now()
                )
            }
            averageMood > Mood.NEUTRAL.ordinal -> {
                Achievement(
                    id = "mood_improvement",
                    title = "Улучшение настроения",
                    description = "Стабильное улучшение настроения",
                    type = AchievementType.MOOD,
                    date = LocalDateTime.now()
                )
            }
            else -> null
        }
    }
}

enum class AchievementType {
    MEDITATION,
    TASK,
    TEST,
    MOOD
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val type: AchievementType,
    val date: LocalDateTime
) 