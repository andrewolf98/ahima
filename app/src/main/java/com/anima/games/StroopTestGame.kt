package com.anima.games

import com.anima.data.diagnostic.КонфигСтрупТеста

class StroopTestGame(private val config: КонфигСтрупТеста) : DiagnosticGameEngine {
    private var currentIndex = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    // Создаем список заданий, где слово и цвет могут не совпадать
    private val задания = config.слова.flatMap { слово ->
        config.цвета.map { цвет ->
            ЗаданиеСтрупа(слово, цвет)
        }
    }.shuffled().take(config.количествоРаундов)

    override fun start() {
        currentIndex = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        if (finished) return false
        
        val текущееЗадание = задания[currentIndex]
        val isCorrect = выбраннаяКатегория == текущееЗадание.цвет
        
        if (isCorrect) {
            score++
        } else {
            mistakes++
        }
        
        currentIndex++
        if (currentIndex >= задания.size) {
            finished = true
        }
        
        return isCorrect
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = задания.size
    
    fun getCurrentTask(): ЗаданиеСтрупа? = 
        if (!finished && currentIndex < задания.size) задания[currentIndex] else null
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
}

data class ЗаданиеСтрупа(
    val слово: String,
    val цвет: String
) 