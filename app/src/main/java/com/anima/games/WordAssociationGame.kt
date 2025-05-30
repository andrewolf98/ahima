package com.anima.games

import com.anima.data.diagnostic.КонфигАссоциацийСлов
import com.anima.data.diagnostic.ПараСлов

class WordAssociationGame(private val config: КонфигАссоциацийСлов) : DiagnosticGameEngine {
    private var currentIndex = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    private val парыСлов = config.пары.shuffled()
    private val использованныеАссоциации = mutableSetOf<String>()

    override fun start() {
        currentIndex = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        использованныеАссоциации.clear()
    }

    fun проверитьАссоциацию(ассоциация: String): Boolean {
        if (finished) return false
        
        val текущаяПара = парыСлов[currentIndex]
        val isCorrect = текущаяПара.ассоциации.any { it.equals(ассоциация, ignoreCase = true) }
        
        if (isCorrect && !использованныеАссоциации.contains(ассоциация)) {
            использованныеАссоциации.add(ассоциация)
            score++
            
            // Проверяем, все ли ассоциации найдены
            if (использованныеАссоциации.size >= текущаяПара.ассоциации.size) {
                currentIndex++
                использованныеАссоциации.clear()
                
                if (currentIndex >= парыСлов.size) {
                    finished = true
                }
            }
            return true
        } else {
            mistakes++
            return false
        }
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        // Не используется в этой игре
        return false
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = парыСлов.sumOf { it.ассоциации.size }
    
    fun getCurrentPair(): ПараСлов? = 
        if (!finished && currentIndex < парыСлов.size) парыСлов[currentIndex] else null
    
    fun getUsedAssociations(): Set<String> = использованныеАссоциации.toSet()
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
    
    fun getRemainingTime(): Long = config.лимитВремениСек * 1000 - getTimeSpent()
} 