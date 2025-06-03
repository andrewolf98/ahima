package com.example.ahima.logic

object RewardSystem {
    fun calculatePoints(phq9Score: Int, gad7Score: Int): Int {
        val base = 100
        val penalty = phq9Score + gad7Score
        return base - penalty
    }

    fun getRewardMessage(points: Int): String {
        return when {
            points >= 90 -> "Вы великолепны! +$points очков"
            points >= 75 -> "Хорошая работа! +$points очков"
            points >= 50 -> "Держитесь! +$points очков"
            else -> "Сегодня сложный день. Баллы: $points"
        }
    }
}
