package com.anima.games

import com.anima.data.diagnostic.КонфигПамятиЧисел
import kotlin.random.Random

class NumberMemoryGame(private val config: КонфигПамятиЧисел) : DiagnosticGameEngine {
    private var currentRound = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    private val последовательности = mutableListOf<List<Int>>()
    private var текущаяПоследовательность: List<Int>? = null
    private var пользовательскийВвод: MutableList<Int> = mutableListOf()

    init {
        // Генерируем последовательности для всех раундов
        repeat(config.количествоРаундов) {
            val последовательность = List(config.длинаПоследовательности) {
                Random.nextInt(config.диапазонЦифр.first, config.диапазонЦифр.last + 1)
            }
            последовательности.add(последовательность)
        }
    }

    override fun start() {
        currentRound = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        показатьСледующуюПоследовательность()
    }

    private fun показатьСледующуюПоследовательность() {
        if (currentRound < последовательности.size) {
            текущаяПоследовательность = последовательности[currentRound]
            пользовательскийВвод.clear()
        } else {
            finished = true
        }
    }

    fun добавитьЦифру(цифра: Int) {
        if (finished || текущаяПоследовательность == null) return
        
        пользовательскийВвод.add(цифра)
        
        // Проверяем, если пользователь ввел все цифры
        if (пользовательскийВвод.size == текущаяПоследовательность!!.size) {
            проверитьПоследовательность()
        }
    }

    private fun проверитьПоследовательность() {
        val правильная = текущаяПоследовательность!!
        val введенная = пользовательскийВвод.toList()
        
        if (правильная == введенная) {
            score++
        } else {
            mistakes++
        }
        
        currentRound++
        показатьСледующуюПоследовательность()
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        // Не используется в этой игре
        return false
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = последовательности.size
    
    fun getCurrentSequence(): List<Int>? = текущаяПоследовательность
    
    fun getCurrentInput(): List<Int> = пользовательскийВвод.toList()
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
} 