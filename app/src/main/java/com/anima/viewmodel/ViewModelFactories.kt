package com.anima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anima.service.AnalyticsService
import com.anima.service.DiagnosticService

// Фабрика для создания DiagnosticViewModel
class DiagnosticViewModelFactory(
    private val diagnosticService: DiagnosticService,
    private val analyticsService: AnalyticsService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiagnosticViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiagnosticViewModel(diagnosticService, analyticsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Фабрика для создания GameViewModel
class GameViewModelFactory(
    private val analyticsService: AnalyticsService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(analyticsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Фабрика для создания RelaxationViewModel
class RelaxationViewModelFactory(
    private val analyticsService: AnalyticsService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RelaxationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RelaxationViewModel(analyticsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// TODO: Добавить фабрики для других ViewModel'ов, если они потребуют зависимостей 