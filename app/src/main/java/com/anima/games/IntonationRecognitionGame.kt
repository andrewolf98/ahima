package com.anima.games

import com.anima.data.diagnostic.КонфигРаспознаванияИнтонаций
import com.anima.data.diagnostic.АудиоКлип
import com.anima.data.diagnostic.Эмоция

class IntonationRecognitionGame(private val config: КонфигРаспознаванияИнтонаций) : DiagnosticGameEngine {
    private var currentIndex = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    private val аудиоКлипы = config.аудиофайлы.shuffled()
    private val использованныеЭмоции = mutableSetOf<String>()

    override fun start() {
        currentIndex = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        использованныеЭмоции.clear()
    }

    fun распознатьЭмоцию(эмоция: String): Boolean {
        if (finished) return false
        
        val текущийКлип = аудиоКлипы[currentIndex]
        val isCorrect = текущийКлип.эмоция.название.equals(эмоция, ignoreCase = true)
        
        if (isCorrect) {
            score++
            использованныеЭмоции.add(эмоция)
            
            currentIndex++
            if (currentIndex >= аудиоКлипы.size) {
                finished = true
            }
        } else {
            mistakes++
        }
        
        return isCorrect
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        return распознатьЭмоцию(выбраннаяКатегория)
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = аудиоКлипы.size
    
    fun getCurrentClip(): АудиоКлип? = 
        if (!finished && currentIndex < аудиоКлипы.size) аудиоКлипы[currentIndex] else null
    
    fun getAvailableEmotions(): List<Эмоция> = 
        config.эмоции.filter { !использованныеЭмоции.contains(it.название) }
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
    
    fun getRemainingTime(): Long = config.лимитВремениСек * 1000 - getTimeSpent()
    
    fun isTimeUp(): Boolean = getRemainingTime() <= 0
} 