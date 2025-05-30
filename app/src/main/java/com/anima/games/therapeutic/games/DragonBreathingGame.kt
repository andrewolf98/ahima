package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BreathState(
    val phase: BreathPhase = BreathPhase.INHALE,
    val fireSize: Float = 0f,
    val breathCount: Int = 0,
    val isCorrect: Boolean = true
)

enum class BreathPhase {
    INHALE,    // 4 секунды
    HOLD,      // 7 секунд
    EXHALE     // 8 секунд
}

class DragonBreathingGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0

    private val _breathState = MutableStateFlow(BreathState())
    val breathState: StateFlow<BreathState> = _breathState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    override fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        _breathState.value = BreathState()
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
        return "Вы выполнили упражнение с $accuracy точностью. " +
               "Продолжайте практиковать дыхание 4-7-8 для снижения тревоги."
    }

    fun onBreathInput(isInhaling: Boolean) {
        if (!isRunning) return

        val currentState = _breathState.value
        val newPhase = when (currentState.phase) {
            BreathPhase.INHALE -> if (isInhaling) BreathPhase.INHALE else BreathPhase.HOLD
            BreathPhase.HOLD -> if (isInhaling) BreathPhase.INHALE else BreathPhase.EXHALE
            BreathPhase.EXHALE -> if (isInhaling) BreathPhase.INHALE else BreathPhase.EXHALE
        }

        val isCorrect = when (currentState.phase) {
            BreathPhase.INHALE -> isInhaling
            BreathPhase.HOLD -> !isInhaling
            BreathPhase.EXHALE -> !isInhaling
        }

        if (!isCorrect) {
            mistakes++
        } else {
            score++
        }

        _breathState.value = currentState.copy(
            phase = newPhase,
            fireSize = calculateFireSize(newPhase),
            breathCount = if (newPhase == BreathPhase.INHALE) currentState.breathCount + 1 else currentState.breathCount,
            isCorrect = isCorrect
        )
    }

    private fun calculateFireSize(phase: BreathPhase): Float {
        return when (phase) {
            BreathPhase.INHALE -> 0.8f
            BreathPhase.HOLD -> 1.0f
            BreathPhase.EXHALE -> 0.2f
        }
    }
} 