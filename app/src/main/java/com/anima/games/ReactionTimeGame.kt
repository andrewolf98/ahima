package com.anima.games

import com.anima.data.diagnostic.КонфигВремениРеакции
import kotlin.random.Random

class ReactionTimeGame(private val config: КонфигВремениРеакции) : DiagnosticGameEngine {
    private var currentRound = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var roundStartTime: Long = 0
    private var finished = false
    private var waitingForReaction = false
    
    private val reactionTimes = mutableListOf<Long>()

    override fun start() {
        currentRound = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        reactionTimes.clear()
        startNewRound()
    }

    private fun startNewRound() {
        if (currentRound >= config.количествоРаундов) {
            finished = true
            return
        }
        
        // Генерируем случайную задержку
        val delay = Random.nextLong(
            config.минимальныйИнтервалМс.toLong(),
            config.максимальныйИнтервалМс.toLong()
        )
        
        // Запускаем таймер для этого раунда
        roundStartTime = System.currentTimeMillis() + delay
        waitingForReaction = true
    }

    fun реакция(): Long {
        if (!waitingForReaction || finished) return -1
        
        val currentTime = System.currentTimeMillis()
        if (currentTime < roundStartTime) {
            // Слишком рано нажали
            mistakes++
            return -1
        }
        
        val reactionTime = currentTime - roundStartTime
        reactionTimes.add(reactionTime)
        score++
        
        waitingForReaction = false
        currentRound++
        startNewRound()
        
        return reactionTime
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        // Не используется в этой игре
        return false
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = config.количествоРаундов
    
    fun getAverageReactionTime(): Long = 
        if (reactionTimes.isEmpty()) 0 else reactionTimes.average().toLong()
    
    fun getMinReactionTime(): Long = 
        if (reactionTimes.isEmpty()) 0 else reactionTimes.minOrNull() ?: 0
    
    fun getMaxReactionTime(): Long = 
        if (reactionTimes.isEmpty()) 0 else reactionTimes.maxOrNull() ?: 0
    
    fun isWaitingForReaction(): Boolean = waitingForReaction
    
    fun getTimeUntilReaction(): Long = 
        if (waitingForReaction) roundStartTime - System.currentTimeMillis() else 0
} 