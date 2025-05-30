package com.anima.data.model

data class PsychologicalProfile(
    val userId: String,
    val anxietyLevel: Int,
    val depressionLevel: Int,
    val stressLevel: Int,
    val sleepQuality: Int,
    val socialSupport: Int,
    val copingStrategies: List<String>,
    val triggers: List<String>,
    val strengths: List<String>
) 