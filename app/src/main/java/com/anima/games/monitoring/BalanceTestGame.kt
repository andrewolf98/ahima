package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.anima.data.monitoring.BalanceTestEntry
import com.anima.data.monitoring.MentalStateMonitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime
import kotlin.random.Random

data class BalanceTestState(
    val ballPosition: Offset = Offset(0.5f, 0.5f),
    val targetPosition: Offset = Offset(0.5f, 0.5f),
    val windForce: Offset = Offset.Zero,
    val score: Int = 0,
    val timeRemaining: Long = 0,
    val feedback: String = "",
    val isGameOver: Boolean = false
)

class BalanceTestGame(
    private val monitor: MentalStateMonitor,
    private val scope: CoroutineScope
) {
    var state by mutableStateOf(BalanceTestState())
        private set

    private val gameDuration = 60000L // 60 секунд
    private val windChangeInterval = 1000L // 1 секунда
    private val maxWindForce = 0.1f
    private var startTime: Long = 0

    fun startGame() {
        startTime = System.currentTimeMillis()
        state = BalanceTestState(
            timeRemaining = gameDuration,
            feedback = "Удерживайте шарик в центре!"
        )
        startWindSimulation()
    }

    private fun startWindSimulation() {
        scope.launch {
            while (!state.isGameOver) {
                delay(windChangeInterval)
                if (!state.isGameOver) {
                    updateWindForce()
                }
            }
        }
    }

    private fun updateWindForce() {
        val newWindForce = Offset(
            Random.nextFloat() * maxWindForce * 2 - maxWindForce,
            Random.nextFloat() * maxWindForce * 2 - maxWindForce
        )
        state = state.copy(windForce = newWindForce)
    }

    fun updateBallPosition(position: Offset) {
        if (state.isGameOver) return

        val newPosition = Offset(
            (position.x + state.windForce.x).coerceIn(0f, 1f),
            (position.y + state.windForce.y).coerceIn(0f, 1f)
        )

        val distance = calculateDistance(newPosition, state.targetPosition)
        val newScore = state.score + calculateScore(distance)

        state = state.copy(
            ballPosition = newPosition,
            score = newScore,
            timeRemaining = gameDuration - (System.currentTimeMillis() - startTime)
        )

        if (state.timeRemaining <= 0) {
            endGame()
        }
    }

    private fun calculateDistance(pos1: Offset, pos2: Offset): Float {
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }

    private fun calculateScore(distance: Float): Int {
        return when {
            distance < 0.1f -> 10
            distance < 0.2f -> 8
            distance < 0.3f -> 6
            distance < 0.4f -> 4
            else -> 2
        }
    }

    private fun endGame() {
        val entry = BalanceTestEntry(
            date = LocalDateTime.now(),
            balanceTime = gameDuration - state.timeRemaining,
            difficulty = getDifficulty()
        )
        monitor.saveBalanceTestEntry(entry)

        state = state.copy(
            isGameOver = true,
            feedback = "Игра окончена! Ваш счет: ${state.score}"
        )
    }

    fun getDifficulty(): Int = when {
        state.score > 500 -> 5
        state.score > 400 -> 4
        state.score > 300 -> 3
        state.score > 200 -> 2
        else -> 1
    }
} 