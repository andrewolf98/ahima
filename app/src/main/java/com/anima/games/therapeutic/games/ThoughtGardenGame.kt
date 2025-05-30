package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class Thought(
    val id: Int,
    val text: String,
    val isPositive: Boolean,
    val position: Pair<Float, Float>,
    val size: Float = 1f,
    val isWatered: Boolean = false
)

data class GardenState(
    val thoughts: List<Thought> = emptyList(),
    val score: Int = 0,
    val waterLevel: Float = 1f,
    val isGameOver: Boolean = false
)

class ThoughtGardenGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0

    private val positiveThoughts = listOf(
        "Я справлюсь с этим",
        "Я становлюсь сильнее",
        "Каждый день - новая возможность",
        "Я заслуживаю счастья",
        "Я контролирую свои мысли",
        "Я выбираю позитив",
        "Я верю в себя",
        "Я делаю всё возможное",
        "Я окружен поддержкой",
        "Я на правильном пути"
    )

    private val negativeThoughts = listOf(
        "У меня ничего не получится",
        "Я недостаточно хорош",
        "Всё бесполезно",
        "Я не справлюсь",
        "Никто меня не понимает",
        "Я всегда ошибаюсь",
        "Я не достоин успеха",
        "Всё слишком сложно",
        "Я не могу это изменить",
        "Я не справлюсь с тревогой"
    )

    private val _gardenState = MutableStateFlow(GardenState())
    val gardenState: StateFlow<GardenState> = _gardenState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    override fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        generateNewThoughts()
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
        return "Вы успешно справились с негативными мыслями с $accuracy точностью. " +
               "Продолжайте практиковать когнитивно-поведенческую терапию для улучшения эмоционального состояния."
    }

    fun onThoughtAction(thoughtId: Int, isWatering: Boolean) {
        if (!isRunning) return

        val currentState = _gardenState.value
        val thought = currentState.thoughts.find { it.id == thoughtId } ?: return

        val isCorrect = if (isWatering) thought.isPositive else !thought.isPositive

        if (isCorrect) {
            score++
            _gardenState.value = currentState.copy(
                thoughts = currentState.thoughts.filter { it.id != thoughtId },
                score = score
            )
            generateNewThoughts()
        } else {
            mistakes++
        }
    }

    private fun generateNewThoughts() {
        val currentState = _gardenState.value
        val newThoughts = mutableListOf<Thought>()

        // Добавляем 2-3 позитивные мысли
        repeat(Random.nextInt(2, 4)) {
            newThoughts.add(
                Thought(
                    id = Random.nextInt(),
                    text = positiveThoughts.random(),
                    isPositive = true,
                    position = Pair(
                        Random.nextFloat() * 0.8f + 0.1f,
                        Random.nextFloat() * 0.8f + 0.1f
                    )
                )
            )
        }

        // Добавляем 1-2 негативные мысли
        repeat(Random.nextInt(1, 3)) {
            newThoughts.add(
                Thought(
                    id = Random.nextInt(),
                    text = negativeThoughts.random(),
                    isPositive = false,
                    position = Pair(
                        Random.nextFloat() * 0.8f + 0.1f,
                        Random.nextFloat() * 0.8f + 0.1f
                    )
                )
            )
        }

        _gardenState.value = currentState.copy(
            thoughts = currentState.thoughts + newThoughts
        )
    }
} 