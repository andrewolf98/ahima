package com.example.ahima.model

data class ExtendedProfile(
    val phq9Score: Int,
    val gad7Score: Int,
    val traumaHistory: Boolean,
    val culturalContext: String,
    val diagnosedConditions: String,
    val riskFactors: List<String>
)
