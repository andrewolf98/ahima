package com.example.ahima.tasks

import com.example.ahima.model.UserProfile

data class Task(val category: String, val description: String)

object DailyTaskEngine {
    fun generateTasks(profile: UserProfile): List<Task> {
        val tasks = mutableListOf<Task>()

        if (profile.phq9Score >= 15) {
            tasks.add(Task("Когнитивное", "Запишите 3 вещи, за которые вы благодарны сегодня"))
        }
        if (profile.gad7Score >= 10) {
            tasks.add(Task("Поведенческое", "Совершите короткую прогулку на свежем воздухе"))
        }

        tasks.add(Task("Медитативное", "Сделайте дыхательную практику перед сном"))
        tasks.add(Task("Социальное", "Позвоните близкому человеку"))

        return tasks
    }
}
