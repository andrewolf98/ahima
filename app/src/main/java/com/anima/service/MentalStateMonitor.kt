package com.anima.service

import com.anima.data.model.MentalState
import kotlinx.coroutines.flow.Flow

interface MentalStateMonitor {
    val currentMentalState: Flow<MentalState>
    suspend fun updateMentalState(mentalState: MentalState)
    suspend fun getMentalStateHistory(): List<MentalState>
} 