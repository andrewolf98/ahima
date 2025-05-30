package com.anima.games

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.model.GameResult
import com.anima.data.model.MentalState
import kotlinx.coroutines.delay
import kotlin.random.Random

class EmotionGame {
    var currentEmotion by mutableStateOf<Emotion?>(null)
        private set
    
    var score by mutableStateOf(0)
        private set
    
    var isGameActive by mutableStateOf(false)
        private set
    
    var timeRemaining by mutableStateOf(0)
        private set
    
    private val emotions = Emotion.values()
    private var gameStartTime = 0L
    
    fun startGame() {
        isGameActive = true
        score = 0
        timeRemaining = GAME_DURATION
        gameStartTime = System.currentTimeMillis()
        generateNewEmotion()
    }
    
    fun endGame(): GameResult {
        isGameActive = false
        return GameResult(
            score = score,
            duration = System.currentTimeMillis() - gameStartTime,
            mentalState = MentalState(
                emotion = currentEmotion?.name ?: "NEUTRAL",
                intensity = score / 10f
            )
        )
    }
    
    fun checkAnswer(selectedEmotion: Emotion): Boolean {
        val isCorrect = selectedEmotion == currentEmotion
        if (isCorrect) {
            score++
        }
        generateNewEmotion()
        return isCorrect
    }
    
    private fun generateNewEmotion() {
        currentEmotion = emotions[Random.nextInt(emotions.size)]
    }
    
    companion object {
        const val GAME_DURATION = 60 // seconds
    }
    
    enum class Emotion {
        HAPPY, SAD, ANGRY, SURPRISED, SCARED, DISGUSTED, NEUTRAL
    }
} 