package com.anima.games

import com.anima.data.diagnostic.КонфигПоискаОтличий
import com.anima.data.diagnostic.Отличие
import com.anima.data.diagnostic.ПараКартинок

class SpotTheDifferenceGame(private val config: КонфигПоискаОтличий) : DiagnosticGameEngine {
    private var currentIndex = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    private val парыКартинок = config.парыКартинок.shuffled()
    private val найденныеОтличия = mutableSetOf<String>()

    override fun start() {
        currentIndex = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        найденныеОтличия.clear()
    }

    fun отметитьОтличие(idОтличия: String): Boolean {
        if (finished) return false
        
        val текущаяПара = парыКартинок[currentIndex]
        val отличие = текущаяПара.отличия.find { it.id == idОтличия }
        
        if (отличие != null && !найденныеОтличия.contains(idОтличия)) {
            найденныеОтличия.add(idОтличия)
            score++
            
            // Проверяем, все ли отличия найдены
            if (найденныеОтличия.size >= текущаяПара.отличия.size) {
                currentIndex++
                найденныеОтличия.clear()
                
                if (currentIndex >= парыКартинок.size) {
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
    
    override fun getTotal(): Int = парыКартинок.sumOf { it.отличия.size }
    
    fun getCurrentPair(): ПараКартинок? = 
        if (!finished && currentIndex < парыКартинок.size) парыКартинок[currentIndex] else null
    
    fun getFoundDifferences(): Set<String> = найденныеОтличия.toSet()
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
    
    fun getRemainingTime(): Long = config.лимитВремениСек * 1000 - getTimeSpent()
} 