package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class Thought(
    val id: Int,
    val text: String,
    val type: ThoughtType,
    val isBalanced: Boolean = false
)

enum class ThoughtType {
    NEGATIVE,
    POSITIVE,
    NEUTRAL
}

data class BalanceState(
    val thoughts: List<Thought> = emptyList(),
    val currentThought: Thought? = null,
    val score: Int = 0,
    val feedback: String = ""
)

class ThoughtBalanceGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0

    private val _balanceState = MutableStateFlow(BalanceState())
    val balanceState: StateFlow<BalanceState> = _balanceState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    private val thoughts = listOf(
        Thought(
            id = 1,
            text = "Я всегда все делаю неправильно",
            type = ThoughtType.NEGATIVE
        ),
        Thought(
            id = 2,
            text = "Я могу справиться с этой задачей",
            type = ThoughtType.POSITIVE
        ),
        Thought(
            id = 3,
            text = "Это просто ситуация, которая требует решения",
            type = ThoughtType.NEUTRAL
        ),
        Thought(
            id = 4,
            text = "Никто меня не понимает",
            type = ThoughtType.NEGATIVE
        ),
        Thought(
            id = 5,
            text = "Я учусь на своих ошибках",
            type = ThoughtType.POSITIVE
        ),
        Thought(
            id = 6,
            text = "Это временная трудность",
            type = ThoughtType.NEUTRAL
        ),
        Thought(
            id = 7,
            text = "Я никогда не достигну успеха",
            type = ThoughtType.NEGATIVE
        ),
        Thought(
            id = 8,
            text = "У меня есть силы справиться с этим",
            type = ThoughtType.POSITIVE
        ),
        Thought(
            id = 9,
            text = "Это часть процесса",
            type = ThoughtType.NEUTRAL
        ),
        Thought(
            id = 10,
            text = "Все против меня",
            type = ThoughtType.NEGATIVE
        ),
        Thought(
            id = 11,
            text = "Я могу найти поддержку",
            type = ThoughtType.POSITIVE
        ),
        Thought(
            id = 12,
            text = "Это требует времени",
            type = ThoughtType.NEUTRAL
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
        return "Вы успешно сбалансировали мысли с $accuracy точностью. " +
               "Продолжайте практиковать позитивное мышление для улучшения эмоционального состояния."
    }

    fun onThoughtSelected(thoughtId: Int) {
        if (!isRunning) return

        val currentState = _balanceState.value
        val thought = currentState.thoughts.find { it.id == thoughtId } ?: return

        _balanceState.value = currentState.copy(
            currentThought = thought
        )
    }

    fun onBalanceSelected(balancedThought: String) {
        if (!isRunning) return

        val currentState = _balanceState.value
        val thought = currentState.currentThought ?: return

        val isCorrect = when (thought.type) {
            ThoughtType.NEGATIVE -> balancedThought.contains("могу") || 
                                 balancedThought.contains("способен") ||
                                 balancedThought.contains("возможно")
            ThoughtType.POSITIVE -> balancedThought.contains("реальность") ||
                                 balancedThought.contains("факт") ||
                                 balancedThought.contains("объективно")
            ThoughtType.NEUTRAL -> balancedThought.contains("перспектива") ||
                                 balancedThought.contains("вариант") ||
                                 balancedThought.contains("возможность")
        }

        if (isCorrect) {
            score += 10
            _balanceState.value = currentState.copy(
                thoughts = currentState.thoughts.map {
                    if (it.id == thought.id) it.copy(isBalanced = true) else it
                },
                currentThought = null,
                score = score,
                feedback = "Отлично! Вы нашли баланс в мысли"
            )

            // Проверяем, все ли мысли сбалансированы
            if (currentState.thoughts.all { it.isBalanced }) {
                generateNewRound()
            }
        } else {
            mistakes++
            _balanceState.value = currentState.copy(
                feedback = "Попробуйте найти более сбалансированную мысль"
            )
        }
    }

    private fun generateNewRound() {
        val shuffledThoughts = thoughts.shuffled().take(3)
        _balanceState.value = BalanceState(
            thoughts = shuffledThoughts
        )
    }
} 