package com.example.ahima.data

import com.example.ahima.model.UserProfile

data class Task(val title: String, val description: String)

object TaskGenerator {

    fun generate(profile: UserProfile): List<Task> {
        val tasks = mutableListOf<Task>()

        if (profile.phq9Score >= 10) {
            tasks.add(Task("Маленький шаг", "Выполните простое действие — например, выпейте стакан воды."))
            tasks.add(Task("Благодарность", "Запишите 3 вещи, за которые вы благодарны."))
        } else {
            tasks.add(Task("Активность", "Прогуляйтесь на свежем воздухе 10–15 минут."))
        }

        if (profile.gad7Score >= 10) {
            tasks.add(Task("Дыхание", "Сделайте дыхательную практику 3-3-3: вдох – 3 сек, пауза – 3 сек, выдох – 3 сек."))
        }

        if (profile.phq9Score < 5 && profile.gad7Score < 5) {
            tasks.add(Task("Поддержание состояния", "Продолжайте в том же духе. Вы молодец!"))
        }

        return tasks
    }
}
