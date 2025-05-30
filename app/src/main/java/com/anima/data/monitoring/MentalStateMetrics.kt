package com.anima.data.monitoring

import java.time.LocalDateTime

data class MoodEntry(
    val date: LocalDateTime,
    val mood: Mood,
    val color: String,
    val intensity: Int // 1-10
)

enum class Mood {
    HAPPY,
    CALM,
    NEUTRAL,
    ANXIOUS,
    SAD,
    ANGRY
}

data class ReactionTimeEntry(
    val date: LocalDateTime,
    val reactionTime: Long, // в миллисекундах
    val difficulty: Int // 1-5
)

data class DrawingEntry(
    val date: LocalDateTime,
    val imageUrl: String,
    val emotion: Emotion,
    val description: String
)

data class MemoryTestEntry(
    val date: LocalDateTime,
    val sequenceLength: Int,
    val isCorrect: Boolean,
    val timeSpent: Long // в миллисекундах
)

data class StroopTestEntry(
    val date: LocalDateTime,
    val errors: Int,
    val averageTime: Long, // в миллисекундах
    val difficulty: Int // 1-5
)

data class BalanceTestEntry(
    val date: LocalDateTime,
    val balanceTime: Long, // в секундах
    val difficulty: Int // 1-5
)

data class WordAssociationEntry(
    val date: LocalDateTime,
    val word: String,
    val association: String,
    val isCommon: Boolean
)

data class RhythmTestEntry(
    val date: LocalDateTime,
    val accuracy: Float, // 0-1
    val pattern: String,
    val difficulty: Int // 1-5
)

data class EmotionSortingEntry(
    val date: LocalDateTime,
    val mistakes: Int,
    val timeSpent: Long, // в миллисекундах
    val difficulty: Int // 1-5
)

data class SleepEntry(
    val date: LocalDateTime,
    val bedTime: LocalDateTime,
    val wakeTime: LocalDateTime,
    val quality: Int // 1-10
)

data class SleepQuestEntry(
    val date: LocalDateTime,
    val bedTime: LocalDateTime,
    val wakeTime: LocalDateTime,
    val quality: Int // 1-10
)

data class MentalStateSummary(
    val date: LocalDateTime,
    val moodScore: Float, // 0-1
    val anxietyScore: Float, // 0-1
    val depressionScore: Float, // 0-1
    val adhdScore: Float, // 0-1
    val sleepScore: Float, // 0-1
    val cognitiveScore: Float, // 0-1
    val notes: String
)

data class MemoryGameEntry(
    val date: LocalDateTime,
    val sequenceLength: Int,
    val isCorrect: Boolean,
    val timeSpent: Long, // в миллисекундах
    val difficulty: Int // 1-5
)

data class AttentionGameEntry(
    val date: LocalDateTime,
    val targetCount: Int,
    val correctCount: Int,
    val falseAlarms: Int,
    val timeSpent: Long, // в миллисекундах
    val difficulty: Int // 1-5
)

data class EmotionGameEntry(
    val date: LocalDateTime,
    val emotion: Emotion,
    val intensity: Int, // 1-10
    val triggers: List<String>,
    val copingStrategies: List<String>,
    val effectiveness: Int // 1-10
)

data class CognitiveGameEntry(
    val date: LocalDateTime,
    val gameType: CognitiveGameType,
    val score: Int,
    val timeSpent: Long, // в миллисекундах
    val difficulty: Int // 1-5
)

enum class CognitiveGameType {
    PUZZLE,
    LOGIC,
    PATTERN_RECOGNITION,
    SPATIAL_REASONING
} 