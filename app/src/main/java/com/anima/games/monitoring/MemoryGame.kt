package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.MemoryGameEntry
import java.time.LocalDateTime
import kotlin.random.Random

data class MemoryGameState(
    val sequence: List<Int> = emptyList(),
    val userInput: List<Int> = emptyList(),
    val isShowingSequence: Boolean = false,
    val score: Int = 0,
    val round: Int = 1,
    val feedback: String = "",
    val isGameOver: Boolean = false
)

class MemoryGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(MemoryGameState())
        private set

    private val maxRounds = 10
    private val sequenceDelay = 1000L // 1 секунда между числами
    private val showTime = 500L // 0.5 секунды на показ числа

    fun startGame() {
        state = MemoryGameState()
        generateNewSequence()
    }

    private fun generateNewSequence() {
        if (state.round > maxRounds) {
            endGame()
            return
        }

        val sequenceLength = 3 + state.round // Увеличиваем длину последовательности с каждым раундом
        val newSequence = List(sequenceLength) { Random.nextInt(1, 10) }
        
        state = state.copy(
            sequence = newSequence,
            userInput = emptyList(),
            isShowingSequence = true,
            feedback = "Запомните последовательность"
        )
    }

    fun onNumberClick(number: Int) {
        if (state.isShowingSequence || state.isGameOver) return

        val newInput = state.userInput + number
        state = state.copy(userInput = newInput)

        if (newInput.size == state.sequence.size) {
            checkSequence()
        }
    }

    private fun checkSequence() {
        val isCorrect = state.sequence == state.userInput
        val newScore = state.score + if (isCorrect) 10 else 0

        state = state.copy(
            score = newScore,
            round = state.round + 1,
            feedback = if (isCorrect) "Правильно!" else "Неправильно. Попробуйте еще раз"
        )

        if (state.round <= maxRounds) {
            generateNewSequence()
        } else {
            endGame()
        }
    }

    private fun endGame() {
        val entry = MemoryGameEntry(
            date = LocalDateTime.now(),
            sequenceLength = state.sequence.size,
            isCorrect = state.sequence == state.userInput,
            timeSpent = 0L, // TODO: Добавить измерение времени
            difficulty = getDifficulty()
        )
        monitor.saveMemoryGameEntry(entry)

        state = state.copy(
            isGameOver = true,
            feedback = "Игра окончена! Ваш счет: ${state.score}"
        )
    }

    fun getDifficulty(): Int = when {
        state.score > 80 -> 5
        state.score > 60 -> 4
        state.score > 40 -> 3
        state.score > 20 -> 2
        else -> 1
    }
} 