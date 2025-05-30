package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.CognitiveGameEntry
import com.anima.data.monitoring.CognitiveGameType
import java.time.LocalDateTime
import kotlin.random.Random

data class CognitiveGameState(
    val gameType: CognitiveGameType = CognitiveGameType.PUZZLE,
    val puzzle: Puzzle? = null,
    val userSolution: List<Int> = emptyList(),
    val score: Int = 0,
    val round: Int = 1,
    val feedback: String = "",
    val isGameOver: Boolean = false
)

data class Puzzle(
    val type: CognitiveGameType,
    val grid: List<List<Int>>,
    val solution: List<Int>
)

class CognitiveGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(CognitiveGameState())
        private set

    private val maxRounds = 10
    private val gridSize = 3

    fun startGame() {
        state = CognitiveGameState()
        generateNewPuzzle()
    }

    private fun generateNewPuzzle() {
        if (state.round > maxRounds) {
            endGame()
            return
        }

        val puzzle = when (state.gameType) {
            CognitiveGameType.PUZZLE -> generateSlidingPuzzle()
            CognitiveGameType.LOGIC -> generateLogicPuzzle()
            CognitiveGameType.PATTERN_RECOGNITION -> generatePatternPuzzle()
            CognitiveGameType.SPATIAL_REASONING -> generateSpatialPuzzle()
        }

        state = state.copy(
            puzzle = puzzle,
            userSolution = emptyList(),
            feedback = "Решите головоломку"
        )
    }

    private fun generateSlidingPuzzle(): Puzzle {
        val numbers = (1..8).toMutableList()
        numbers.add(0) // пустая клетка
        numbers.shuffle()

        val grid = numbers.chunked(3)
        val solution = (1..8).toList() + 0

        return Puzzle(CognitiveGameType.PUZZLE, grid, solution)
    }

    private fun generateLogicPuzzle(): Puzzle {
        // Простая логическая головоломка с числами
        val grid = List(3) { List(3) { Random.nextInt(1, 10) } }
        val solution = grid.flatten().sorted()

        return Puzzle(CognitiveGameType.LOGIC, grid, solution)
    }

    private fun generatePatternPuzzle(): Puzzle {
        // Головоломка на распознавание паттернов
        val pattern = List(3) { Random.nextInt(1, 5) }
        val grid = List(3) { row ->
            List(3) { col ->
                if (col < 2) pattern[row] else Random.nextInt(1, 5)
            }
        }
        val solution = pattern

        return Puzzle(CognitiveGameType.PATTERN_RECOGNITION, grid, solution)
    }

    private fun generateSpatialPuzzle(): Puzzle {
        // Пространственная головоломка
        val grid = List(3) { List(3) { Random.nextInt(1, 9) } }
        val solution = grid.flatten().sorted()

        return Puzzle(CognitiveGameType.SPATIAL_REASONING, grid, solution)
    }

    fun onNumberClick(number: Int) {
        if (state.isGameOver || state.puzzle == null) return

        val newSolution = state.userSolution + number
        state = state.copy(userSolution = newSolution)

        if (newSolution.size == state.puzzle.solution.size) {
            checkSolution()
        }
    }

    private fun checkSolution() {
        val puzzle = state.puzzle ?: return
        val isCorrect = state.userSolution == puzzle.solution
        val newScore = state.score + if (isCorrect) 10 else 0

        state = state.copy(
            score = newScore,
            round = state.round + 1,
            feedback = if (isCorrect) "Правильно!" else "Неправильно. Попробуйте еще раз"
        )

        if (state.round <= maxRounds) {
            generateNewPuzzle()
        } else {
            endGame()
        }
    }

    private fun endGame() {
        val entry = CognitiveGameEntry(
            date = LocalDateTime.now(),
            gameType = state.gameType,
            score = state.score,
            timeSpent = 0L, // TODO: Добавить измерение времени
            difficulty = getDifficulty()
        )
        monitor.saveCognitiveGameEntry(entry)

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