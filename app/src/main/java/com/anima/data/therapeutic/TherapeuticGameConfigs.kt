package com.anima.data.therapeutic

object TherapeuticGameConfigs {
    val thoughtBalance = TherapeuticGameConfig(
        duration = 180000L, // 3 минуты
        difficulty = GameDifficulty.MEDIUM,
        description = "Баланс мыслей - игра, помогающая развивать навыки когнитивной переоценки и эмоциональной регуляции"
    )

    val colorTherapy = TherapeuticGameConfig(
        duration = 240000L, // 4 минуты
        difficulty = GameDifficulty.EASY,
        description = "Цветотерапия - игра, помогающая развивать визуальное восприятие и эмоциональную регуляцию через работу с цветами"
    )
} 