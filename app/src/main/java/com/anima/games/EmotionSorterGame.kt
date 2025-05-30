package com.anima.games

import com.anima.data.diagnostic.Эмоция
import com.anima.data.diagnostic.КонфигЭмоциональногоСортера

// Интерфейс для игровой логики диагностических мини-игр
interface DiagnosticGameEngine {
    fun start()
    fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean
    fun isFinished(): Boolean
    fun getScore(): Int
    fun getTotal(): Int
}

// Реализация для Эмоционального сортера
class EmotionSorterGame(private val config: КонфигЭмоциональногоСортера) : DiagnosticGameEngine {
    private var currentIndex = 0
    private var score = 0
    private val эмоции = config.эмоции.shuffled()
    private val total = эмоции.size
    private var finished = false

    override fun start() {
        currentIndex = 0
        score = 0
        finished = false
    }

    // Пользователь выбирает категорию для текущей эмоции
    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        if (finished) return false
        val правильная = эмоции[currentIndex].название
        val isCorrect = правильная.equals(выбраннаяКатегория, ignoreCase = true)
        if (isCorrect) score++
        currentIndex++
        if (currentIndex >= total) finished = true
        return isCorrect
    }

    override fun isFinished(): Boolean = finished
    override fun getScore(): Int = score
    override fun getTotal(): Int = total

    // Получить текущую эмоцию для отображения
    fun getCurrentEmotion(): Эмоция? = if (!finished && currentIndex < total) эмоции[currentIndex] else null
} 