package com.anima.games

import com.anima.data.diagnostic.КонфигFlankerTask
import kotlin.random.Random

class FlankerTaskGame(private val config: КонфигFlankerTask) : DiagnosticGameEngine {
    private var currentRound = 0
    private var score = 0
    private var mistakes = 0
    private var startTime: Long = 0
    private var finished = false
    
    private val стрелки = listOf("←", "→")
    private val задания = mutableListOf<ЗаданиеFlanker>()
    private var текущееЗадание: ЗаданиеFlanker? = null

    init {
        // Генерируем задания для всех раундов
        repeat(config.количествоРаундов) {
            val центральнаяСтрелка = стрелки.random()
            val фланкеры = List(2) { стрелки.random() }
            val конгруэнтность = фланкеры.all { it == центральнаяСтрелка }
            
            задания.add(ЗаданиеFlanker(
                центральнаяСтрелка = центральнаяСтрелка,
                фланкеры = фланкеры,
                конгруэнтность = конгруэнтность
            ))
        }
    }

    override fun start() {
        currentRound = 0
        score = 0
        mistakes = 0
        finished = false
        startTime = System.currentTimeMillis()
        показатьСледующееЗадание()
    }

    private fun показатьСледующееЗадание() {
        if (currentRound < задания.size) {
            текущееЗадание = задания[currentRound]
        } else {
            finished = true
        }
    }

    fun проверитьОтвет(направление: String): Boolean {
        if (finished || текущееЗадание == null) return false
        
        val isCorrect = направление == текущееЗадание!!.центральнаяСтрелка
        
        if (isCorrect) {
            score++
        } else {
            mistakes++
        }
        
        currentRound++
        показатьСледующееЗадание()
        
        return isCorrect
    }

    override fun submitAnswer(эмоция: String, выбраннаяКатегория: String): Boolean {
        return проверитьОтвет(выбраннаяКатегория)
    }

    override fun isFinished(): Boolean = finished
    
    override fun getScore(): Int = score
    
    override fun getTotal(): Int = задания.size
    
    fun getCurrentTask(): ЗаданиеFlanker? = текущееЗадание
    
    fun getTimeSpent(): Long = System.currentTimeMillis() - startTime
    
    fun getRemainingTime(): Long = config.лимитВремениСек * 1000 - getTimeSpent()
    
    fun isTimeUp(): Boolean = getRemainingTime() <= 0
    
    // Статистика по конгруэнтным и неконгруэнтным заданиям
    fun getCongruentScore(): Int = 
        задания.filterIndexed { index, задание ->
            задание.конгруэнтность && index < currentRound
        }.count { задание ->
            задание.центральнаяСтрелка == текущееЗадание?.центральнаяСтрелка
        }
    
    fun getIncongruentScore(): Int = 
        задания.filterIndexed { index, задание ->
            !задание.конгруэнтность && index < currentRound
        }.count { задание ->
            задание.центральнаяСтрелка == текущееЗадание?.центральнаяСтрелка
        }
}

data class ЗаданиеFlanker(
    val центральнаяСтрелка: String,
    val фланкеры: List<String>,
    val конгруэнтность: Boolean
) 