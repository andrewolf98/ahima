package com.example.ahima.neuro

import com.example.ahima.model.UserProfile
import com.example.ahima.engine.GameLevel
import com.example.ahima.engine.GameLevelGenerator

object NeuroGameEngine {
    fun createAdaptiveLevel(profile: UserProfile): GameLevel {
        val pattern = NeuroPatternGenerator.generate(profile)
        val baseLevel = GameLevelGenerator.generate(profile)

        return baseLevel.copy(
            difficulty = when (pattern.complexity) {
                3 -> "Hard"
                2 -> "Medium"
                else -> "Easy"
            },
            theme = pattern.style
        )
    }
}
