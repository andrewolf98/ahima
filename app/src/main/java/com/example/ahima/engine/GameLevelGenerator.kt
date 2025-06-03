package com.example.ahima.engine

import com.example.ahima.model.UserProfile

data class GameLevel(val difficulty: String, val theme: String, val challenge: String)

object GameLevelGenerator {
    fun generate(profile: UserProfile): GameLevel {
        val difficulty = when {
            profile.phq9Score + profile.gad7Score >= 25 -> "Hard"
            profile.phq9Score + profile.gad7Score >= 15 -> "Medium"
            else -> "Easy"
        }

        val theme = when {
            profile.phq9Score >= 10 -> "Focus"
            profile.gad7Score >= 10 -> "Calm"
            else -> "Explore"
        }

        val challenge = when (difficulty) {
            "Hard" -> "Тревожный лабиринт"
            "Medium" -> "Задачи осознанности"
            else -> "Простые мини-испытания"
        }

        return GameLevel(difficulty, theme, challenge)
    }
}
