package com.example.ahima.relax

data class BreathingPattern(val name: String, val inhale: Int, val hold: Int, val exhale: Int)

object BreathingExercise {
    fun getRecommendedPattern(anxietyLevel: Int): BreathingPattern {
        return when {
            anxietyLevel > 14 -> BreathingPattern("4-7-8", 4, 7, 8)
            anxietyLevel > 7 -> BreathingPattern("Box", 4, 4, 4)
            else -> BreathingPattern("Calm", 3, 2, 4)
        }
    }
}
