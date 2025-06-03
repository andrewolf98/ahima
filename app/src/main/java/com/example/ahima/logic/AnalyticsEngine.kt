package com.example.ahima.logic

import com.example.ahima.model.UserProfile

data class Insight(val label: String, val value: String)

object AnalyticsEngine {
    fun getInsights(profile: UserProfile): List<Insight> {
        val mood = when {
            profile.phq9Score >= 20 -> "Сильная депрессия"
            profile.phq9Score >= 10 -> "Умеренная депрессия"
            else -> "Лёгкая или отсутствует"
        }

        val anxiety = when {
            profile.gad7Score >= 15 -> "Сильная тревожность"
            profile.gad7Score >= 8 -> "Умеренная тревожность"
            else -> "Низкая тревожность"
        }

        return listOf(
            Insight("Настроение", mood),
            Insight("Тревожность", anxiety)
        )
    }
}
