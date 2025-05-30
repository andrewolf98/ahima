package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class Sound(
    val id: Int,
    val name: String,
    val description: String,
    val category: SoundCategory,
    val isPlaying: Boolean = false
)

enum class SoundCategory {
    NATURE,
    MUSIC,
    ANIMALS,
    CITY,
    HOUSEHOLD
}

data class DetectiveState(
    val sounds: List<Sound> = emptyList(),
    val targetSound: Sound? = null,
    val score: Int = 0,
    val feedback: String = ""
)

class SoundDetectiveGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0

    private val _detectiveState = MutableStateFlow(DetectiveState())
    val detectiveState: StateFlow<DetectiveState> = _detectiveState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    private val sounds = listOf(
        Sound(
            id = 1,
            name = "Дождь",
            description = "Звук падающих капель дождя",
            category = SoundCategory.NATURE
        ),
        Sound(
            id = 2,
            name = "Волны",
            description = "Шум морских волн",
            category = SoundCategory.NATURE
        ),
        Sound(
            id = 3,
            name = "Ветер",
            description = "Шум ветра в листве",
            category = SoundCategory.NATURE
        ),
        Sound(
            id = 4,
            name = "Пианино",
            description = "Мелодия на пианино",
            category = SoundCategory.MUSIC
        ),
        Sound(
            id = 5,
            name = "Гитара",
            description = "Звук акустической гитары",
            category = SoundCategory.MUSIC
        ),
        Sound(
            id = 6,
            name = "Барабаны",
            description = "Ритм барабанов",
            category = SoundCategory.MUSIC
        ),
        Sound(
            id = 7,
            name = "Птицы",
            description = "Пение птиц",
            category = SoundCategory.ANIMALS
        ),
        Sound(
            id = 8,
            name = "Собака",
            description = "Лай собаки",
            category = SoundCategory.ANIMALS
        ),
        Sound(
            id = 9,
            name = "Кошка",
            description = "Мяуканье кошки",
            category = SoundCategory.ANIMALS
        ),
        Sound(
            id = 10,
            name = "Транспорт",
            description = "Шум городского транспорта",
            category = SoundCategory.CITY
        ),
        Sound(
            id = 11,
            name = "Толпа",
            description = "Гул толпы",
            category = SoundCategory.CITY
        ),
        Sound(
            id = 12,
            name = "Строительство",
            description = "Звуки стройки",
            category = SoundCategory.CITY
        ),
        Sound(
            id = 13,
            name = "Чайник",
            description = "Свист чайника",
            category = SoundCategory.HOUSEHOLD
        ),
        Sound(
            id = 14,
            name = "Пылесос",
            description = "Шум пылесоса",
            category = SoundCategory.HOUSEHOLD
        ),
        Sound(
            id = 15,
            name = "Стиральная машина",
            description = "Работа стиральной машины",
            category = SoundCategory.HOUSEHOLD
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
        return "Вы успешно распознали звуки с $accuracy точностью. " +
               "Продолжайте развивать навыки аудиального восприятия для улучшения концентрации внимания."
    }

    fun onSoundSelected(soundId: Int) {
        if (!isRunning) return

        val currentState = _detectiveState.value
        val selectedSound = currentState.sounds.find { it.id == soundId } ?: return
        val targetSound = currentState.targetSound ?: return

        if (selectedSound.id == targetSound.id) {
            score++
            _detectiveState.value = currentState.copy(
                feedback = "Правильно! Это ${targetSound.name} - ${targetSound.description}"
            )
            generateNewRound()
        } else {
            mistakes++
            _detectiveState.value = currentState.copy(
                feedback = "Попробуйте еще раз. Найдите звук: ${targetSound.description}"
            )
        }
    }

    private fun generateNewRound() {
        val shuffledSounds = sounds.shuffled().take(4)
        val targetSound = shuffledSounds.random()

        _detectiveState.value = DetectiveState(
            sounds = shuffledSounds,
            targetSound = targetSound
        )
    }
} 