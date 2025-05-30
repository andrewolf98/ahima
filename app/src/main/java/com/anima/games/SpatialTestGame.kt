package com.anima.games

import com.anima.data.diagnostic.КонфигПространственногоТеста
import com.anima.data.diagnostic.Узор

class SpatialTestGame(private val config: КонфигПространственногоТеста) : DiagnosticGameEngine {
    private var currentIndex = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    private val узоры = config.узоры.shuffled()
    private val использованныеУзоры = mutableSetOf<String>()

    override fun start() {
        currentIndex = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        использованныеУзоры.clear()
    }

    fun проверитьОтвет(ответ: String): Boolean {
        if (finished) return false
        
        val текущийУзор = узоры[currentIndex]
        val isCorrect = текущийУзор.id == ответ
        
        if (isCorrect) {
            score++
            использованныеУзоры.add(ответ)
            
            currentIndex++
            if (currentIndex >= узоры.size) {
                finished = true
            }
        } else {
            mistakes++
        }
        
        return isCorrect
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        return проверитьОтвет(выбраннаяКатегория)
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = узоры.size
    
    fun getCurrentPattern(): Узор? = 
        if (!finished && currentIndex < узоры.size) узоры[currentIndex] else null
    
    fun getUsedPatterns(): Set<String> = использованныеУзоры
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
    
    fun getRemainingTime(): Long = config.лимитВремениСек * 1000 - getTimeSpent()
    
    fun isTimeUp(): Boolean = getRemainingTime() <= 0
    
    // Дополнительные методы для пространственного теста
    fun повернутьУзор(угол: Int): Узор? {
        val текущийУзор = getCurrentPattern() ?: return null
        // Здесь должна быть логика поворота узора
        return текущийУзор
    }
    
    fun отразитьУзор(по горизонтали: Boolean): Узор? {
        val текущийУзор = getCurrentPattern() ?: return null
        // Здесь должна быть логика отражения узора
        return текущийУзор
    }
} 