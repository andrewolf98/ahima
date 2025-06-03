package com.example.ahima.logic

import com.example.ahima.model.UserProfile

data class NeuroLevel(val id: Int, val title: String, val difficulty: String, val style: String)

object LevelGenerator {

    fun generate(profile: UserProfile): List<NeuroLevel> {
        val levels = mutableListOf<NeuroLevel>()

        val baseDifficulty = when {
            profile.phq9Score >= 20 || profile.gad7Score >= 15 -> "Высокая"
            profile.phq9Score >= 10 || profile.gad7Score >= 10 -> "Средняя"
            else -> "Низкая"
        }

        val baseStyle = when {
            profile.phq9Score >= 15 -> "Минимализм"
            profile.gad7Score >= 15 -> "Нейронные паттерны"
            else -> "Цветотерапия"
        }

        repeat(3) {
            levels.add(
                NeuroLevel(
                    id = it + 1,
                    title = "Нейроуровень ${it + 1}",
                    difficulty = baseDifficulty,
                    style = baseStyle
                )
            )
        }

        return levels
    }
}
