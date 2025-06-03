package com.example.ahima.neuro

import com.example.ahima.model.UserProfile

data class NeuroPattern(val style: String, val complexity: Int)

object NeuroPatternGenerator {
    fun generate(profile: UserProfile): NeuroPattern {
        val scoreSum = profile.phq9Score + profile.gad7Score
        val style = when {
            scoreSum >= 25 -> "Глубокая тьма"
            scoreSum >= 15 -> "Тревожная симметрия"
            else -> "Светлое спокойствие"
        }
        val complexity = when {
            scoreSum >= 25 -> 3
            scoreSum >= 15 -> 2
            else -> 1
        }
        return NeuroPattern(style, complexity)
    }
}
