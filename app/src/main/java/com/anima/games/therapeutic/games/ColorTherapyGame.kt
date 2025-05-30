package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class ColorTask(
    val id: Int,
    val targetColor: Color,
    val emotion: Emotion,
    val isCompleted: Boolean = false
)

enum class Emotion {
    CALM,
    ENERGY,
    HAPPINESS,
    FOCUS,
    CREATIVITY
}

data class TherapyState(
    val tasks: List<ColorTask> = emptyList(),
    val currentTask: ColorTask? = null,
    val score: Int = 0,
    val feedback: String = ""
)

class ColorTherapyGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0

    private val _therapyState = MutableStateFlow(TherapyState())
    val therapyState: StateFlow<TherapyState> = _therapyState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    private val colorTasks = listOf(
        ColorTask(
            id = 1,
            targetColor = Color(0xFF81C784), // Спокойный зеленый
            emotion = Emotion.CALM
        ),
        ColorTask(
            id = 2,
            targetColor = Color(0xFFFFB74D), // Энергичный оранжевый
            emotion = Emotion.ENERGY
        ),
        ColorTask(
            id = 3,
            targetColor = Color(0xFFFFEB3B), // Радостный желтый
            emotion = Emotion.HAPPINESS
        ),
        ColorTask(
            id = 4,
            targetColor = Color(0xFF64B5F6), // Сосредоточенный синий
            emotion = Emotion.FOCUS
        ),
        ColorTask(
            id = 5,
            targetColor = Color(0xFFBA68C8), // Креативный фиолетовый
            emotion = Emotion.CREATIVITY
        )
    )

    override fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        generateNewRound()
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
        return "Вы успешно справились с цветотерапией с $accuracy точностью. " +
               "Продолжайте практиковать работу с цветами для улучшения эмоционального состояния."
    }

    fun onTaskSelected(taskId: Int) {
        if (!isRunning) return

        val currentState = _therapyState.value
        val task = currentState.tasks.find { it.id == taskId } ?: return

        _therapyState.value = currentState.copy(
            currentTask = task
        )
    }

    fun onColorSelected(selectedColor: Color) {
        if (!isRunning) return

        val currentState = _therapyState.value
        val task = currentState.currentTask ?: return

        // Проверяем близость выбранного цвета к целевому
        val colorDistance = calculateColorDistance(selectedColor, task.targetColor)
        val isCorrect = colorDistance < 0.2f // Порог близости цветов

        if (isCorrect) {
            score += 10
            _therapyState.value = currentState.copy(
                tasks = currentState.tasks.map {
                    if (it.id == task.id) it.copy(isCompleted = true) else it
                },
                currentTask = null,
                score = score,
                feedback = "Отлично! Вы нашли правильный цвет для ${getEmotionText(task.emotion)}"
            )

            // Проверяем, все ли задачи выполнены
            if (currentState.tasks.all { it.isCompleted }) {
                generateNewRound()
            }
        } else {
            mistakes++
            _therapyState.value = currentState.copy(
                feedback = "Попробуйте найти более подходящий цвет для ${getEmotionText(task.emotion)}"
            )
        }
    }

    private fun generateNewRound() {
        val shuffledTasks = colorTasks.shuffled()
        _therapyState.value = TherapyState(
            tasks = shuffledTasks
        )
    }

    private fun calculateColorDistance(color1: Color, color2: Color): Float {
        val r1 = color1.red
        val g1 = color1.green
        val b1 = color1.blue
        val r2 = color2.red
        val g2 = color2.green
        val b2 = color2.blue

        return kotlin.math.sqrt(
            (r1 - r2) * (r1 - r2) +
            (g1 - g2) * (g1 - g2) +
            (b1 - b2) * (b1 - b2)
        )
    }

    private fun getEmotionText(emotion: Emotion): String = when (emotion) {
        Emotion.CALM -> "спокойствия"
        Emotion.ENERGY -> "энергии"
        Emotion.HAPPINESS -> "радости"
        Emotion.FOCUS -> "сосредоточенности"
        Emotion.CREATIVITY -> "креативности"
    }
} 