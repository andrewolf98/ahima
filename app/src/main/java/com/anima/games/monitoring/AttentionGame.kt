package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.AttentionGameEntry
import java.time.LocalDateTime
import kotlin.random.Random

data class AttentionGameState(
    val targets: List<Target> = emptyList(),
    val userSelections: List<Int> = emptyList(),
    val score: Int = 0,
    val round: Int = 1,
    val feedback: String = "",
    val isGameOver: Boolean = false
)

data class Target(
    val id: Int,
    val isTarget: Boolean,
    val position: Pair<Int, Int>
)

class AttentionGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(AttentionGameState())
        private set

    private val maxRounds = 10
    private val gridSize = 5
    private val targetCount = 3
    private val distractorsCount = 7

    fun startGame() {
        state = AttentionGameState()
        generateNewRound()
    }

    private fun generateNewRound() {
        if (state.round > maxRounds) {
            endGame()
            return
        }

        val targets = mutableListOf<Target>()
        val positions = mutableSetOf<Pair<Int, Int>>()

        // Генерируем целевые объекты
        repeat(targetCount) {
            var position: Pair<Int, Int>
            do {
                position = Pair(
                    Random.nextInt(gridSize),
                    Random.nextInt(gridSize)
                )
            } while (position in positions)
            positions.add(position)
            targets.add(Target(it, true, position))
        }

        // Генерируем отвлекающие объекты
        repeat(distractorsCount) {
            var position: Pair<Int, Int>
            do {
                position = Pair(
                    Random.nextInt(gridSize),
                    Random.nextInt(gridSize)
                )
            } while (position in positions)
            positions.add(position)
            targets.add(Target(targetCount + it, false, position))
        }

        state = state.copy(
            targets = targets.shuffled(),
            userSelections = emptyList(),
            feedback = "Найдите все целевые объекты"
        )
    }

    fun onTargetClick(targetId: Int) {
        if (state.isGameOver) return

        val target = state.targets.find { it.id == targetId } ?: return
        val newSelections = state.userSelections + targetId

        state = state.copy(userSelections = newSelections)

        if (newSelections.size == targetCount) {
            checkRound()
        }
    }

    private fun checkRound() {
        val correctSelections = state.userSelections.count { id ->
            state.targets.find { it.id == id }?.isTarget == true
        }
        val falseAlarms = state.userSelections.count { id ->
            state.targets.find { it.id == id }?.isTarget == false
        }

        val roundScore = (correctSelections * 10) - (falseAlarms * 5)
        val newScore = state.score + roundScore

        state = state.copy(
            score = newScore,
            round = state.round + 1,
            feedback = "Правильно: $correctSelections, Ошибок: $falseAlarms"
        )

        if (state.round <= maxRounds) {
            generateNewRound()
        } else {
            endGame()
        }
    }

    private fun endGame() {
        val entry = AttentionGameEntry(
            date = LocalDateTime.now(),
            targetCount = targetCount,
            correctCount = state.userSelections.count { id ->
                state.targets.find { it.id == id }?.isTarget == true
            },
            falseAlarms = state.userSelections.count { id ->
                state.targets.find { it.id == id }?.isTarget == false
            },
            timeSpent = 0L, // TODO: Добавить измерение времени
            difficulty = getDifficulty()
        )
        monitor.saveAttentionGameEntry(entry)

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