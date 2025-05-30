package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.RhythmTestEntry
import java.time.LocalDateTime
import kotlin.random.Random

data class RhythmTestState(
    val pattern: String = "",
    val userInput: String = "",
    val isPlaying: Boolean = false,
    val score: Int = 0,
    val round: Int = 1,
    val feedback: String = "",
    val isGameOver: Boolean = false
)

class RhythmTestGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(RhythmTestState())
        private set

    private val maxRounds = 10
    private val patterns = listOf(
        "x-x-x-x", // 4/4
        "x-x-x-x-x-x", // 6/8
        "x--x--x--x--", // 4/4 with rests
        "x-x-x-x-x-x-x-x", // 8/8
        "x--x--x--x--x--" // 5/4
    )

    fun startGame() {
        state = RhythmTestState()
        generateNewPattern()
    }

    private fun generateNewPattern() {
        if (state.round > maxRounds) {
            endGame()
            return
        }

        val pattern = patterns[Random.nextInt(patterns.size)]
        state = state.copy(
            pattern = pattern,
            userInput = "",
            isPlaying = false,
            feedback = "Повторите ритм: $pattern"
        )
    }

    fun onTap() {
        if (state.isGameOver) return

        val newInput = state.userInput + "x"
        state = state.copy(userInput = newInput)

        if (newInput.length == state.pattern.length) {
            checkPattern()
        }
    }

    fun onRest() {
        if (state.isGameOver) return

        val newInput = state.userInput + "-"
        state = state.copy(userInput = newInput)

        if (newInput.length == state.pattern.length) {
            checkPattern()
        }
    }

    private fun checkPattern() {
        val accuracy = calculateAccuracy(state.pattern, state.userInput)
        val newScore = state.score + calculateScore(accuracy)

        state = state.copy(
            score = newScore,
            round = state.round + 1,
            feedback = if (accuracy > 0.8f) "Отлично!" else "Попробуйте еще раз"
        )

        if (state.round <= maxRounds) {
            generateNewPattern()
        } else {
            endGame()
        }
    }

    private fun calculateAccuracy(pattern: String, input: String): Float {
        var correct = 0
        for (i in pattern.indices) {
            if (i < input.length && pattern[i] == input[i]) {
                correct++
            }
        }
        return correct.toFloat() / pattern.length
    }

    private fun calculateScore(accuracy: Float): Int {
        return (accuracy * 10).toInt()
    }

    private fun endGame() {
        val entry = RhythmTestEntry(
            date = LocalDateTime.now(),
            accuracy = state.score.toFloat() / (maxRounds * 10),
            pattern = state.pattern,
            difficulty = getDifficulty()
        )
        monitor.saveRhythmTestEntry(entry)

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