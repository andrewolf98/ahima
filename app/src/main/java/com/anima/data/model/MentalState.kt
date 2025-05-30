package com.anima.data.model

data class MentalState(
    val emotion: String = "NEUTRAL",
    val intensity: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
) 