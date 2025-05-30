package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.SleepQuestEntry
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class SleepQuestState(
    val bedTime: LocalDateTime? = null,
    val wakeTime: LocalDateTime? = null,
    val quality: Int = 5,
    val streak: Int = 0,
    val feedback: String = "",
    val isSaved: Boolean = false
)

class SleepQuestGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(SleepQuestState())
        private set

    fun setBedTime(time: LocalDateTime) {
        state = state.copy(
            bedTime = time,
            isSaved = false
        )
    }

    fun setWakeTime(time: LocalDateTime) {
        state = state.copy(
            wakeTime = time,
            isSaved = false
        )
    }

    fun setQuality(quality: Int) {
        state = state.copy(
            quality = quality.coerceIn(1, 10),
            isSaved = false
        )
    }

    suspend fun saveEntry() {
        val bedTime = state.bedTime ?: return
        val wakeTime = state.wakeTime ?: return

        val entry = SleepQuestEntry(
            date = LocalDateTime.now(),
            bedTime = bedTime,
            wakeTime = wakeTime,
            quality = state.quality
        )

        monitor.saveSleepEntry(entry)

        val sleepDuration = ChronoUnit.HOURS.between(bedTime, wakeTime)
        val newStreak = if (isGoodSleep(sleepDuration, state.quality)) {
            state.streak + 1
        } else {
            0
        }

        state = state.copy(
            isSaved = true,
            streak = newStreak,
            feedback = generateFeedback(sleepDuration, state.quality)
        )
    }

    private fun isGoodSleep(duration: Long, quality: Int): Boolean {
        return duration in 7..9 && quality >= 7
    }

    private fun generateFeedback(duration: Long, quality: Int): String {
        val feedback = StringBuilder()

        when {
            duration < 6 -> feedback.append("Вы спали меньше 6 часов. Старайтесь спать 7-9 часов.")
            duration > 9 -> feedback.append("Вы спали больше 9 часов. Оптимальная продолжительность сна - 7-9 часов.")
            else -> feedback.append("Отличная продолжительность сна!")
        }

        feedback.append("\n")

        when {
            quality < 5 -> feedback.append("Качество сна низкое. Попробуйте расслабляющие техники перед сном.")
            quality < 7 -> feedback.append("Качество сна среднее. Обратите внимание на условия сна.")
            else -> feedback.append("Отличное качество сна!")
        }

        if (state.streak > 0) {
            feedback.append("\nВаша серия хорошего сна: ${state.streak} дней!")
        }

        return feedback.toString()
    }

    fun getSleepTips(): List<String> = listOf(
        "Ложитесь спать в одно и то же время",
        "Избегайте кофеина за 6 часов до сна",
        "Создайте комфортные условия для сна",
        "Не используйте гаджеты за час до сна",
        "Практикуйте расслабляющие техники перед сном"
    )
} 