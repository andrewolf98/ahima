package com.anima.service

import com.anima.data.*
import com.anima.data.diagnostic.DiagnosticResult
import com.anima.data.diagnostic.RiskLevel
import com.anima.data.db.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

class AnalyticsService(
    private val dailyProgressDao: DailyProgressDao,
    private val diagnosticEventDao: DiagnosticEventDao,
    private val gameEventDao: GameEventDao,
    private val relaxationEventDao: RelaxationEventDao,
    private val generatedContentConfigDao: GeneratedContentConfigDao
) {
    fun calculateMoodTrend(progressHistory: List<DailyProgress>): MoodTrend {
        if (progressHistory.isEmpty()) return MoodTrend.NEUTRAL

        val recentProgress = progressHistory.takeLast(7)
        val moodValues = recentProgress.map { it.mood.ordinal }
        val averageMood = moodValues.average().roundToInt()

        return when {
            averageMood < Mood.NEUTRAL.ordinal -> MoodTrend.DECREASING
            averageMood > Mood.NEUTRAL.ordinal -> MoodTrend.INCREASING
            else -> MoodTrend.NEUTRAL
        }
    }

    fun calculateAnxietyTrend(progressHistory: List<DailyProgress>): AnxietyTrend {
        if (progressHistory.isEmpty()) return AnxietyTrend.STABLE

        val recentProgress = progressHistory.takeLast(7)
        val anxietyValues = recentProgress.map { it.anxiety }
        val averageAnxiety = anxietyValues.average()
        val firstWeekAverage = anxietyValues.take(3).average()
        val lastWeekAverage = anxietyValues.takeLast(3).average()

        return when {
            lastWeekAverage > firstWeekAverage + 2 -> AnxietyTrend.INCREASING
            lastWeekAverage < firstWeekAverage - 2 -> AnxietyTrend.DECREASING
            else -> AnxietyTrend.STABLE
        }
    }

    fun calculateProductivityScore(progressHistory: List<DailyProgress>): Int {
        if (progressHistory.isEmpty()) return 0

        val recentProgress = progressHistory.takeLast(7)
        val productivityValues = recentProgress.map { it.productivity }
        return productivityValues.average().roundToInt()
    }

    fun calculateMeditationConsistency(progressHistory: List<DailyProgress>): Float {
        if (progressHistory.isEmpty()) return 0f

        val recentProgress = progressHistory.takeLast(30)
        val daysWithMeditation = recentProgress.count { it.meditationMinutes > 0 }
        return (daysWithMeditation.toFloat() / recentProgress.size) * 100
    }

    fun generateRecommendations(
        userState: UserState,
        progressHistory: List<DailyProgress>
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()

        // Анализ настроения
        val moodTrend = calculateMoodTrend(progressHistory)
        if (moodTrend == MoodTrend.DECREASING) {
            recommendations.add(
                Recommendation(
                    type = RecommendationType.MOOD,
                    title = "Практикуйте позитивное мышление",
                    description = "Попробуйте технику благодарности или медитацию на позитивные мысли"
                )
            )
        }

        // Анализ тревожности
        val anxietyTrend = calculateAnxietyTrend(progressHistory)
        if (anxietyTrend == AnxietyTrend.INCREASING) {
            recommendations.add(
                Recommendation(
                    type = RecommendationType.ANXIETY,
                    title = "Увеличьте время медитации",
                    description = "Добавьте 5-10 минут к вашей ежедневной практике"
                )
            )
        }

        // Анализ продуктивности
        val productivityScore = calculateProductivityScore(progressHistory)
        if (productivityScore < 6) {
            recommendations.add(
                Recommendation(
                    type = RecommendationType.PRODUCTIVITY,
                    title = "Планируйте день",
                    description = "Составьте список приоритетных задач на день"
                )
            )
        }

        // Анализ медитаций
        val meditationConsistency = calculateMeditationConsistency(progressHistory)
        if (meditationConsistency < 70f) {
            recommendations.add(
                Recommendation(
                    type = RecommendationType.MEDITATION,
                    title = "Установите напоминания",
                    description = "Настройте ежедневные напоминания о медитации"
                )
            )
        }

        return recommendations
    }

    fun calculateProgressMetrics(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        progressHistory: List<DailyProgress>
    ): ProgressMetrics {
        val filteredProgress = progressHistory.filter {
            it.date.isAfter(startDate) && it.date.isBefore(endDate)
        }

        val totalDays = ChronoUnit.DAYS.between(startDate, endDate)
        val completedTasks = filteredProgress.sumOf { it.completedTasks.size }
        val totalMeditationMinutes = filteredProgress.sumOf { it.meditationMinutes }
        val averageSleepHours = filteredProgress.map { it.sleepHours }.average()
        val averageAnxiety = filteredProgress.map { it.anxiety }.average()
        val averageProductivity = filteredProgress.map { it.productivity }.average()

        return ProgressMetrics(
            period = "${startDate.toLocalDate()} - ${endDate.toLocalDate()}",
            totalDays = totalDays.toInt(),
            completedTasks = completedTasks,
            totalMeditationMinutes = totalMeditationMinutes,
            averageSleepHours = averageSleepHours.toFloat(),
            averageAnxiety = averageAnxiety.toFloat(),
            averageProductivity = averageProductivity.toFloat()
        )
    }

    suspend fun logDiagnosticEvent(event: DiagnosticEvent) {
        val entity = DiagnosticEventEntity(
            userId = event.userId,
            testId = event.testId,
            dateTime = event.date,
            eventType = event.eventType.name,
            questionId = event.questionId,
            answer = event.answer,
            score = event.score,
            resultId = event.resultId
        )
        diagnosticEventDao.insertDiagnosticEvent(entity)
        println("Logged Diagnostic Event: $event")
    }

    suspend fun logGameEvent(event: GameEvent) {
        val entity = GameEventEntity(
            userId = event.userId,
            gameId = event.gameId,
            dateTime = event.date,
            eventType = event.eventType.name,
            levelId = event.levelId,
            score = event.score,
            timeSpent = event.timeSpent,
            success = event.success
        )
        gameEventDao.insertGameEvent(entity)
        println("Logged Game Event: $event")
    }

    suspend fun logRelaxationEvent(event: RelaxationEvent) {
        val entity = RelaxationEventEntity(
            userId = event.userId,
            sessionId = event.sessionId,
            dateTime = event.date,
            eventType = event.eventType.name,
            duration = event.duration,
            rating = event.rating,
            feedback = event.feedback
        )
        relaxationEventDao.insertRelaxationEvent(entity)
        println("Logged Relaxation Event: $event")
    }

    suspend fun saveDailyProgress(progress: DailyProgress) {
        val entity = DailyProgressEntity(
            date = progress.date,
            mood = progress.mood,
            anxiety = progress.anxiety,
            productivity = progress.productivity,
            meditationMinutes = progress.meditationMinutes,
            tasksCompletedJson = ""
        )
        dailyProgressDao.insertDailyProgress(entity)
        println("Saved Daily Progress: $progress")
    }

    suspend fun saveGeneratedContentConfig(config: GeneratedContentConfig) {
        val entity = GeneratedContentConfigEntity(
            configId = config.gameConfig?.gameId ?: config.levelConfig?.levelId ?: config.relaxationConfig?.sessionId ?: "",
            userId = "current_user_id",
            contentType = config.contentType.name,
            configJson = ""
        )
        generatedContentConfigDao.insertGeneratedContentConfig(entity)
        println("Saved Generated Content Config: $config")
    }

    fun getUserProfileAnalytics(userId: String): UserProfileAnalytics {
        val diagnosticEvents = diagnosticEventDao.getDiagnosticEvents(userId)
        val gameEvents = gameEventDao.getGameEvents(userId)
        val relaxationEvents = relaxationEventDao.getRelaxationEvents(userId)
        val dailyProgressList = dailyProgressDao.getDailyProgress(userId)

        val diagnosticHistory: List<DiagnosticResult> = emptyList()
        val gamePerformance: List<GameEvent> = gameEvents.map { GameEvent(it.userId, it.gameId, it.dateTime, GameEventType.valueOf(it.eventType), it.levelId, it.score, it.timeSpent, it.success) }
        val relaxationHistory: List<RelaxationEvent> = relaxationEvents.map { RelaxationEvent(it.userId, it.sessionId, it.dateTime, RelaxationEventType.valueOf(it.eventType), it.duration, it.rating, it.feedback) }
        val dailyProgress: List<DailyProgress> = dailyProgressList.map { DailyProgress(it.date, it.mood, it.anxiety, it.productivity, it.meditationMinutes, 0) }

        val currentMood: Int? = null
        val currentAnxiety: Int? = null

        return UserProfileAnalytics(
            userId = userId,
            diagnosticHistory = diagnosticHistory,
            gamePerformance = gamePerformance,
            relaxationHistory = relaxationHistory,
            dailyProgress = dailyProgress,
            currentMood = currentMood,
            currentAnxiety = currentAnxiety
        )
    }
}

enum class MoodTrend {
    INCREASING,
    DECREASING,
    NEUTRAL
}

enum class AnxietyTrend {
    INCREASING,
    DECREASING,
    STABLE
}

enum class RecommendationType {
    MOOD,
    ANXIETY,
    PRODUCTIVITY,
    MEDITATION
}

data class Recommendation(
    val type: RecommendationType,
    val title: String,
    val description: String
)

data class ProgressMetrics(
    val period: String,
    val totalDays: Int,
    val completedTasks: Int,
    val totalMeditationMinutes: Int,
    val averageSleepHours: Float,
    val averageAnxiety: Float,
    val averageProductivity: Float
) 