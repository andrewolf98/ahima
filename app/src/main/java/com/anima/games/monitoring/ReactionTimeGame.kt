package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.ReactionTimeEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.time.LocalDateTime
import kotlin.random.Random

data class ReactionTimeState(
    val targetPosition: Offset? = null,
    val isTargetVisible: Boolean = false,
    val startTime: Long = 0,
    val reactionTime: Long = 0,
    val score: Int = 0,
    val round: Int = 1,
    val feedback: String = "",
    val isGameOver: Boolean = false
)

class ReactionTimeGame(
    private val monitor: MentalStateMonitor,
    private val scope: CoroutineScope
) {
    var state by mutableStateOf(ReactionTimeState())
        private set

    private var targetAppearTime: Long = 0
    private var mistakes = 0
    private val maxRounds = 10
    private val minDelay = 1000L // 1 секунда
    private val maxDelay = 3000L // 3 секунды

    fun startGame() {
        state = ReactionTimeState()
        scheduleNextTarget()
    }

    private fun scheduleNextTarget() {
        scope.launch {
            val delay = Random.nextLong(minDelay, maxDelay)
            delay(delay)
            
            if (state.round <= maxRounds) {
                val x = Random.nextFloat() * 0.8f + 0.1f
                val y = Random.nextFloat() * 0.8f + 0.1f
                
                state = state.copy(
                    targetPosition = Offset(x, y),
                    isTargetVisible = true,
                    startTime = System.currentTimeMillis()
                )
                targetAppearTime = System.currentTimeMillis()
            } else {
                endGame()
            }
        }
    }

    fun onTargetClicked() {
        if (!state.isTargetVisible) return

        val reactionTime = System.currentTimeMillis() - targetAppearTime
        val isCorrect = reactionTime < 1000 // Реакция должна быть быстрее 1 секунды

        if (isCorrect) {
            val newScore = state.score + calculateScore(reactionTime)
            state = state.copy(
                score = newScore,
                reactionTime = reactionTime,
                round = state.round + 1,
                feedback = "Отлично! +${calculateScore(reactionTime)} очков"
            )
        } else {
            mistakes++
            state = state.copy(
                feedback = "Слишком медленно! Попробуйте быстрее"
            )
        }

        state = state.copy(isTargetVisible = false)
        scheduleNextTarget()
    }

    private fun calculateScore(reactionTime: Long): Int {
        return when {
            reactionTime < 200 -> 10
            reactionTime < 400 -> 8
            reactionTime < 600 -> 6
            reactionTime < 800 -> 4
            else -> 2
        }
    }

    private fun endGame() {
        scope.launch {
            val entry = ReactionTimeEntry(
                date = LocalDateTime.now(),
                reactionTime = state.reactionTime,
                difficulty = 1
            )
            monitor.saveReactionTimeEntry(entry)

            state = state.copy(
                isGameOver = true,
                feedback = "Игра окончена! Ваш счет: ${state.score}"
            )
        }
    }

    fun getDifficulty(): Int = when {
        state.score > 80 -> 5
        state.score > 60 -> 4
        state.score > 40 -> 3
        state.score > 20 -> 2
        else -> 1
    }
} 