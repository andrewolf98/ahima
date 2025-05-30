package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class FacePart(
    val id: Int,
    val type: FacePartType,
    val emotion: Emotion,
    val position: Pair<Float, Float>,
    val isPlaced: Boolean = false
)

enum class FacePartType {
    EYES,
    MOUTH,
    EYEBROWS
}

enum class Emotion {
    HAPPY,
    SAD,
    ANGRY,
    SURPRISED,
    SCARED,
    NEUTRAL
}

data class PuzzleState(
    val parts: List<FacePart> = emptyList(),
    val targetEmotion: Emotion = Emotion.HAPPY,
    val score: Int = 0,
    val feedback: String = ""
)

class EmotionPuzzleGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0

    private val _puzzleState = MutableStateFlow(PuzzleState())
    val puzzleState: StateFlow<PuzzleState> = _puzzleState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    private val emotionParts = mapOf(
        Emotion.HAPPY to mapOf(
            FacePartType.EYES to "Счастливые глаза",
            FacePartType.MOUTH to "Улыбка",
            FacePartType.EYEBROWS to "Приподнятые брови"
        ),
        Emotion.SAD to mapOf(
            FacePartType.EYES to "Грустные глаза",
            FacePartType.MOUTH to "Опущенные уголки губ",
            FacePartType.EYEBROWS to "Опущенные брови"
        ),
        Emotion.ANGRY to mapOf(
            FacePartType.EYES to "Суженные глаза",
            FacePartType.MOUTH to "Сжатые губы",
            FacePartType.EYEBROWS to "Нахмуренные брови"
        ),
        Emotion.SURPRISED to mapOf(
            FacePartType.EYES to "Широко открытые глаза",
            FacePartType.MOUTH to "Открытый рот",
            FacePartType.EYEBROWS to "Высоко поднятые брови"
        ),
        Emotion.SCARED to mapOf(
            FacePartType.EYES to "Испуганные глаза",
            FacePartType.MOUTH to "Приоткрытый рот",
            FacePartType.EYEBROWS to "Приподнятые брови"
        ),
        Emotion.NEUTRAL to mapOf(
            FacePartType.EYES to "Нейтральные глаза",
            FacePartType.MOUTH to "Нейтральный рот",
            FacePartType.EYEBROWS to "Нейтральные брови"
        )
    )

    override fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        generateNewPuzzle()
        _remainingTime.value = config.duration
    }

    override fun pause() {
        isRunning = false
    }

    override fun resume() {
        isRunning = true
    }

    override fun stop() {
        isRunning = false
    }

    override fun getScore(): Int = score

    override fun getProgress(): Float {
        val timeSpent = System.currentTimeMillis() - startTime
        return (timeSpent.toFloat() / config.duration).coerceIn(0f, 1f)
    }

    override fun getTimeSpent(): Long = System.currentTimeMillis() - startTime

    override fun isFinished(): Boolean = getProgress() >= 1f

    override fun getTherapeuticFeedback(): String {
        val accuracy = if (mistakes == 0) "отличная" else "хорошая"
        return "Вы успешно распознали эмоции с $accuracy точностью. " +
               "Продолжайте развивать эмоциональный интеллект для лучшего понимания себя и других."
    }

    fun onPartPlaced(partId: Int, position: Pair<Float, Float>) {
        if (!isRunning) return

        val currentState = _puzzleState.value
        val part = currentState.parts.find { it.id == partId } ?: return

        val newParts = currentState.parts.map {
            if (it.id == partId) it.copy(
                position = position,
                isPlaced = true
            ) else it
        }

        _puzzleState.value = currentState.copy(parts = newParts)

        // Проверяем, все ли части на месте
        if (newParts.all { it.isPlaced }) {
            checkEmotion()
        }
    }

    private fun checkEmotion() {
        val currentState = _puzzleState.value
        val placedParts = currentState.parts.filter { it.isPlaced }
        
        // Проверяем, соответствует ли собранное лицо целевой эмоции
        val isCorrect = placedParts.all { it.emotion == currentState.targetEmotion }
        
        if (isCorrect) {
            score++
            _puzzleState.value = currentState.copy(
                feedback = "Правильно! Это ${currentState.targetEmotion.name.lowercase()}"
            )
            generateNewPuzzle()
        } else {
            mistakes++
            _puzzleState.value = currentState.copy(
                feedback = "Попробуйте еще раз. Соберите лицо, выражающее ${currentState.targetEmotion.name.lowercase()}"
            )
        }
    }

    private fun generateNewPuzzle() {
        val targetEmotion = Emotion.values().random()
        val parts = mutableListOf<FacePart>()

        // Генерируем части лица для целевой эмоции
        FacePartType.values().forEach { type ->
            parts.add(
                FacePart(
                    id = Random.nextInt(),
                    type = type,
                    emotion = targetEmotion,
                    position = Pair(
                        Random.nextFloat() * 0.8f + 0.1f,
                        Random.nextFloat() * 0.8f + 0.1f
                    )
                )
            )
        }

        // Добавляем несколько "лишних" частей
        repeat(2) {
            val randomEmotion = Emotion.values().filter { it != targetEmotion }.random()
            val randomType = FacePartType.values().random()
            parts.add(
                FacePart(
                    id = Random.nextInt(),
                    type = randomType,
                    emotion = randomEmotion,
                    position = Pair(
                        Random.nextFloat() * 0.8f + 0.1f,
                        Random.nextFloat() * 0.8f + 0.1f
                    )
                )
            )
        }

        _puzzleState.value = PuzzleState(
            parts = parts,
            targetEmotion = targetEmotion
        )
    }
} 