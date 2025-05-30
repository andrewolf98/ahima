package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class Beat(
    val id: Int,
    val position: Float,
    val type: BeatType,
    val isHit: Boolean = false
)

enum class BeatType {
    NORMAL,
    SPECIAL
}

data class RhythmState(
    val beats: List<Beat> = emptyList(),
    val score: Int = 0,
    val combo: Int = 0,
    val feedback: String = ""
)

class RhythmMeditationGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0
    private var combo = 0

    private val _rhythmState = MutableStateFlow(RhythmState())
    val rhythmState: StateFlow<RhythmState> = _rhythmState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    override fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        generateNewRhythm()
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
        return "Вы успешно поддерживали ритм с $accuracy точностью. " +
               "Продолжайте практиковать ритм-медитацию для улучшения концентрации и снятия стресса."
    }

    fun onBeatHit(beatId: Int) {
        if (!isRunning) return

        val currentState = _rhythmState.value
        val beat = currentState.beats.find { it.id == beatId } ?: return

        if (!beat.isHit) {
            combo++
            score += when (beat.type) {
                BeatType.NORMAL -> 1
                BeatType.SPECIAL -> 2
            } * combo

            _rhythmState.value = currentState.copy(
                beats = currentState.beats.map {
                    if (it.id == beatId) it.copy(isHit = true) else it
                },
                score = score,
                combo = combo,
                feedback = when (beat.type) {
                    BeatType.NORMAL -> "Хорошо! +${combo} очков"
                    BeatType.SPECIAL -> "Отлично! +${2 * combo} очков"
                }
            )

            // Проверяем, все ли биты пройдены
            if (currentState.beats.all { it.isHit }) {
                generateNewRhythm()
            }
        }
    }

    fun onBeatMissed(beatId: Int) {
        if (!isRunning) return

        val currentState = _rhythmState.value
        val beat = currentState.beats.find { it.id == beatId } ?: return

        if (!beat.isHit) {
            mistakes++
            combo = 0

            _rhythmState.value = currentState.copy(
                combo = 0,
                feedback = "Пропущен бит! Комбо сброшен"
            )
        }
    }

    private fun generateNewRhythm() {
        val beats = mutableListOf<Beat>()
        var currentPosition = 0f

        // Генерируем 8-12 битов
        repeat(Random.nextInt(8, 13)) {
            val beatType = if (Random.nextFloat() < 0.2f) BeatType.SPECIAL else BeatType.NORMAL
            beats.add(
                Beat(
                    id = Random.nextInt(),
                    position = currentPosition,
                    type = beatType
                )
            )
            currentPosition += 0.1f + Random.nextFloat() * 0.1f
        }

        _rhythmState.value = RhythmState(
            beats = beats
        )
    }
} 