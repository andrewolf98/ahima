package com.example.ahima.scheduler

import com.example.ahima.model.UserProfile

data class ScheduledTask(val title: String, val time: String)

object TaskScheduler {
    fun getSchedule(profile: UserProfile): List<ScheduledTask> {
        val tasks = mutableListOf<ScheduledTask>()

        if (profile.phq9Score >= 15) {
            tasks.add(ScheduledTask("Дневник благодарности", "08:00"))
        }
        if (profile.gad7Score >= 10) {
            tasks.add(ScheduledTask("Прогулка", "12:00"))
        }

        tasks.add(ScheduledTask("Медитация", "21:00"))
        tasks.add(ScheduledTask("Позвонить другу", "18:00"))

        return tasks
    }
}
