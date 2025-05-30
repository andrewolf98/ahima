package com.anima.games

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.model.GameResult
import com.anima.data.model.MentalState
import kotlin.random.Random

class CognitiveGame {
    var currentSequence by mutableStateOf<List<Int>>(emptyList())
        private set
    
    var userSequence by mutableStateOf<List<Int>>(emptyList())
        private set
    
    var score by mutableStateOf(0)
        private set
    
    var isGameActive by mutableStateOf(false)
        private set
    
    var timeRemaining by mutableStateOf(0)
        private set
    
    private var gameStartTime = 0L
    private var sequenceLength = INITIAL_SEQUENCE_LENGTH
    
    fun startGame() {
        isGameActive = true
        score = 0
        timeRemaining = GAME_DURATION
        gameStartTime = System.currentTimeMillis()
        sequenceLength = INITIAL_SEQUENCE_LENGTH
        generateNewSequence()
    }
    
    fun endGame(): GameResult {
        isGameActive = false
        return GameResult(
            score = score,
            duration = System.currentTimeMillis() - gameStartTime,
            mentalState = MentalState(
                emotion = "FOCUSED",
                intensity = score / 10f
            )
        )
    }
    
    fun addToUserSequence(number: Int) {
        if (!isGameActive) return
        userSequence = userSequence + number
        checkSequence()
    }
    
    private fun checkSequence() {
        if (userSequence.size == currentSequence.size) {
            val isCorrect = userSequence == currentSequence
            if (isCorrect) {
                score++
                sequenceLength++
            }
            userSequence = emptyList()
            generateNewSequence()
        }
    }
    
    private fun generateNewSequence() {
        currentSequence = List(sequenceLength) { Random.nextInt(1, 5) }
    }
    
    companion object {
        const val GAME_DURATION = 60 // seconds
        const val INITIAL_SEQUENCE_LENGTH = 3
    }
} 