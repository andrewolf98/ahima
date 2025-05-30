package com.anima.games

import com.anima.data.diagnostic.КонфигСортировкиКарточек
import com.anima.data.diagnostic.Карточка
import com.anima.data.diagnostic.ПравилоСортировки

class CardSortingGame(private val config: КонфигСортировкиКарточек) : DiagnosticGameEngine {
    private var currentRound = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    private val карточки = config.карточки.shuffled()
    private val правила = config.правила.shuffled()
    private var текущееПравило: ПравилоСортировки? = null
    private val отсортированныеКарточки = mutableMapOf<String, MutableList<Карточка>>()

    init {
        // Инициализируем категории для сортировки
        правила.forEach { правило ->
            отсортированныеКарточки[правило.id] = mutableListOf()
        }
    }

    override fun start() {
        currentRound = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        
        // Очищаем предыдущие результаты
        отсортированныеКарточки.values.forEach { it.clear() }
        
        // Устанавливаем первое правило
        текущееПравило = правила.firstOrNull()
    }

    fun сортироватьКарточку(карточка: Карточка, категория: String): Boolean {
        if (finished || текущееПравило == null) return false
        
        val isCorrect = проверитьСортировку(карточка, категория)
        
        if (isCorrect) {
            score++
            отсортированныеКарточки[категория]?.add(карточка)
            
            // Проверяем, все ли карточки отсортированы
            if (всеКарточкиОтсортированы()) {
                currentRound++
                if (currentRound >= правила.size) {
                    finished = true
                } else {
                    текущееПравило = правила[currentRound]
                    // Перемешиваем карточки для нового правила
                    карточки.shuffle()
                    отсортированныеКарточки.values.forEach { it.clear() }
                }
            }
        } else {
            mistakes++
        }
        
        return isCorrect
    }

    private fun проверитьСортировку(карточка: Карточка, категория: String): Boolean {
        // Здесь должна быть логика проверки правильности сортировки
        // В зависимости от текущего правила
        return true // Заглушка, нужно реализовать реальную логику
    }

    private fun всеКарточкиОтсортированы(): Boolean {
        return отсортированныеКарточки.values.sumOf { it.size } == карточки.size
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        // Не используется в этой игре
        return false
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = карточки.size * правила.size
    
    fun getCurrentRule(): ПравилоСортировки? = текущееПравило
    
    fun getUnsortedCards(): List<Карточка> = 
        карточки.filter { карточка ->
            !отсортированныеКарточки.values.any { it.contains(карточка) }
        }
    
    fun getSortedCards(category: String): List<Карточка> = 
        отсортированныеКарточки[category] ?: emptyList()
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
} 