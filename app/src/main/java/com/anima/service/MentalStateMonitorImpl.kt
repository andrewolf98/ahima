package com.anima.service

import com.anima.data.model.MentalState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.CopyOnWriteArrayList

class MentalStateMonitorImpl : MentalStateMonitor {
    private val _currentMentalState = MutableStateFlow(MentalState())
    override val currentMentalState: Flow<MentalState> = _currentMentalState.asStateFlow()
    
    private val mentalStateHistory = CopyOnWriteArrayList<MentalState>()
    
    override suspend fun updateMentalState(mentalState: MentalState) {
        _currentMentalState.value = mentalState
        mentalStateHistory.add(mentalState)
    }
    
    override suspend fun getMentalStateHistory(): List<MentalState> {
        return mentalStateHistory.toList()
    }
} 