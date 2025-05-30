package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.EmotionGameEntry
import com.anima.data.monitoring.Emotion
import java.time.LocalDateTime

data class EmotionGameState(
    val selectedEmotion: Emotion? = null,
    val intensity: Int = 5,
    val triggers: List<String> = emptyList(),
    val copingStrategies: List<String> = emptyList(),
    val effectiveness: Int = 5,
    val feedback: String = "",
    val isSaved: Boolean = false
)

class EmotionGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(EmotionGameState())
        private set

    private val commonTriggers = listOf(
        "Стресс на работе",
        "Конфликт с близкими",
        "Финансовые проблемы",
        "Проблемы со здоровьем",
        "Недостаток сна",
        "Переутомление",
        "Одиночество",
        "Неопределенность будущего"
    )

    private val commonStrategies = listOf(
        "Глубокое дыхание",
        "Медитация",
        "Физические упражнения",
        "Прогулка на свежем воздухе",
        "Разговор с другом",
        "Ведение дневника",
        "Прослушивание музыки",
        "Хобби"
    )

    fun selectEmotion(emotion: Emotion) {
        state = state.copy(
            selectedEmotion = emotion,
            isSaved = false
        )
    }

    fun setIntensity(intensity: Int) {
        state = state.copy(
            intensity = intensity.coerceIn(1, 10),
            isSaved = false
        )
    }

    fun addTrigger(trigger: String) {
        if (trigger.isNotBlank() && trigger !in state.triggers) {
            state = state.copy(
                triggers = state.triggers + trigger,
                isSaved = false
            )
        }
    }

    fun removeTrigger(trigger: String) {
        state = state.copy(
            triggers = state.triggers - trigger,
            isSaved = false
        )
    }

    fun addCopingStrategy(strategy: String) {
        if (strategy.isNotBlank() && strategy !in state.copingStrategies) {
            state = state.copy(
                copingStrategies = state.copingStrategies + strategy,
                isSaved = false
            )
        }
    }

    fun removeCopingStrategy(strategy: String) {
        state = state.copy(
            copingStrategies = state.copingStrategies - strategy,
            isSaved = false
        )
    }

    fun setEffectiveness(effectiveness: Int) {
        state = state.copy(
            effectiveness = effectiveness.coerceIn(1, 10),
            isSaved = false
        )
    }

    suspend fun saveEntry() {
        val emotion = state.selectedEmotion ?: return

        val entry = EmotionGameEntry(
            date = LocalDateTime.now(),
            emotion = emotion,
            intensity = state.intensity,
            triggers = state.triggers,
            copingStrategies = state.copingStrategies,
            effectiveness = state.effectiveness
        )
        monitor.saveEmotionGameEntry(entry)

        state = state.copy(
            isSaved = true,
            feedback = generateFeedback(emotion, state.intensity, state.effectiveness)
        )
    }

    private fun generateFeedback(emotion: Emotion, intensity: Int, effectiveness: Int): String {
        val feedback = StringBuilder()

        // Анализ интенсивности
        when {
            intensity >= 8 -> feedback.append("Сильная интенсивность эмоции. ")
            intensity >= 5 -> feedback.append("Умеренная интенсивность эмоции. ")
            else -> feedback.append("Низкая интенсивность эмоции. ")
        }

        // Рекомендации по эмоции
        when (emotion) {
            Emotion.HAPPY -> feedback.append("Отлично! Продолжайте практиковать позитивное мышление.")
            Emotion.CALM -> feedback.append("Хорошее состояние. Поддерживайте баланс.")
            Emotion.NEUTRAL -> feedback.append("Стабильное состояние. Следите за изменениями.")
            Emotion.ANXIOUS -> feedback.append("Тревожность повышена. Попробуйте техники релаксации.")
            Emotion.SAD -> feedback.append("Грусть - нормальная эмоция. Позвольте себе её прожить.")
            Emotion.ANGRY -> feedback.append("Гнев требует внимания. Найдите конструктивный выход.")
        }

        // Оценка эффективности стратегий
        if (effectiveness >= 7) {
            feedback.append("\nВаши стратегии работают хорошо!")
        } else {
            feedback.append("\nВозможно, стоит попробовать другие стратегии.")
        }

        return feedback.toString()
    }

    fun getCommonTriggers(): List<String> = commonTriggers
    fun getCommonStrategies(): List<String> = commonStrategies
} 