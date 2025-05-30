package com.anima.data

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyProgress(
    val date: LocalDate,
    val mood: Int, // Например, от 1 до 5
    val anxiety: Int, // Например, от 1 до 5
    val productivity: Int, // Например, от 1 до 5
    val meditationMinutes: Int,
    val tasksCompleted: Int
)

data class Trend(
    val value: Double,
    val recommendations: String
)

// Дополнительные модели данных для аналитики

// События диагностики
data class DiagnosticEvent(
    val userId: String,
    val testId: String,
    val date: LocalDateTime,
    val eventType: DiagnosticEventType, // Начало теста, завершение, ответ на вопрос и т.д.
    val questionId: String? = null, // ID вопроса, если событие связано с ответом
    val answer: Int? = null, // Ответ пользователя
    val score: Int? = null, // Балл за тест или подшкалу, если событие связано с результатом
    val resultId: String? = null // ID результата, если тест завершен
)

enum class DiagnosticEventType {
    TEST_STARTED,
    QUESTION_ANSWERED,
    TEST_COMPLETED,
    RESULT_VIEWED
}

// События игр
data class GameEvent(
    val userId: String,
    val gameId: String,
    val date: LocalDateTime,
    val eventType: GameEventType, // Начало игры, завершение уровня, сбор предмета и т.д.
    val levelId: String? = null, // ID уровня
    val score: Int? = null, // Баллы в игре
    val timeSpent: Long? = null, // Время, проведенное в игре/на уровне (в секундах)
    val success: Boolean? = null // Успешно ли пройден уровень/игра
)

enum class GameEventType {
    GAME_STARTED,
    LEVEL_STARTED,
    LEVEL_COMPLETED,
    GAME_COMPLETED,
    ITEM_COLLECTED,
    CHALLENGE_SUCCEEDED,
    CHALLENGE_FAILED
}

// События релаксации
data class RelaxationEvent(
    val userId: String,
    val sessionId: String,
    val date: LocalDateTime,
    val eventType: RelaxationEventType, // Начало сессии, завершение, оценка
    val duration: Long? = null, // Длительность сессии (в секундах)
    val rating: Int? = null, // Оценка пользователя (например, от 1 до 5)
    val feedback: String? = null // Текстовый отзыв
)

enum class RelaxationEventType {
    SESSION_STARTED,
    SESSION_COMPLETED,
    SESSION_RATED
}

// Модели для взаимодействия с нейросетью

// Входные данные для генерации контента
data class UserProfileAnalytics(
    val userId: String,
    val diagnosticHistory: List<DiagnosticResult>, // История результатов диагностики
    val gamePerformance: List<GameEvent>, // Данные о производительности в играх
    val relaxationHistory: List<RelaxationEvent>, // История сессий релаксации
    val dailyProgress: List<DailyProgress>, // Динамика ежедневного прогресса
    val currentMood: Int? = null, // Текущее настроение (опционально)
    val currentAnxiety: Int? = null // Текущий уровень тревоги (опционально)
    // Могут быть добавлены другие параметры, например, предпочтения пользователя
)

// Выходные данные от нейросети - конфигурация контента
data class GeneratedContentConfig(
    val contentType: GeneratedContentType,
    val gameConfig: GameConfig? = null,
    val levelConfig: LevelConfig? = null,
    val relaxationConfig: RelaxationConfig? = null
)

enum class GeneratedContentType {
    GAME,
    LEVEL,
    RELAXATION_SESSION
}

data class GameConfig(
    val gameId: String,
    val difficulty: DifficultyLevel,
    val themes: List<String>,
    val objectives: List<String>
)

data class LevelConfig(
    val levelId: String,
    val gameId: String,
    val layoutParameters: Map<String, Any>,
    val challengeType: ChallengeType,
    val emotionalFocus: EmotionalFocus
)

enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD,
    ADAPTIVE // Сложность, подстраивающаяся под пользователя
}

enum class ChallengeType {
    MEMORY,
    ATTENTION,
    PROBLEM_SOLVING,
    EMOTIONAL_REGULATION
}

enum class EmotionalFocus {
    CALMING,
    ENERGIZING,
    FOCUSING,
    RELAXING
}

data class RelaxationConfig(
    val sessionId: String,
    val type: RelaxationType,
    val duration: Int, // in minutes
    val audioParameters: Map<String, Any>,
    val visualParameters: Map<String, Any>,
    val guidedMeditationScript: String? = null
)

enum class RelaxationType {
    GUIDED_MEDITATION,
    SOUNDSCAPE,
    BREATHING_EXERCISES,
    VISUALIZATION
}

// Дополнительные модели данных для аналитики, если потребуются 