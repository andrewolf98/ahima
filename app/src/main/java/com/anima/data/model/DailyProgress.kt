package com.anima.data.model

import java.time.LocalDate

data class DailyProgress(
    val date: LocalDate,
    val completedTasks: Int,
    val mood: Mood,
    val sleepHours: Float,
    val tasksCompleted: List<String> = emptyList()
) 