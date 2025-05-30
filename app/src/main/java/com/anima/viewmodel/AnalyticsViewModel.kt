package com.anima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.DailyProgress
import com.anima.data.Trend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AnalyticsViewModel : ViewModel() {

    private val _progressHistory = MutableStateFlow<List<DailyProgress>>(emptyList())
    val progressHistory: StateFlow<List<DailyProgress>> = _progressHistory.asStateFlow()

    private val _moodTrend = MutableStateFlow<Trend?>(null)
    val moodTrend: StateFlow<Trend?> = _moodTrend.asStateFlow()

    private val _anxietyTrend = MutableStateFlow<Trend?>(null)
    val anxietyTrend: StateFlow<Trend?> = _anxietyTrend.asStateFlow()

    private val _productivityTrend = MutableStateFlow<Trend?>(null)
    val productivityTrend: StateFlow<Trend?> = _productivityTrend.asStateFlow()

    private val _meditationConsistency = MutableStateFlow<Trend?>(null)
    val meditationConsistency: StateFlow<Trend?> = _meditationConsistency.asStateFlow()

    init {
        loadProgressHistory()
    }

    private fun loadProgressHistory() {
        viewModelScope.launch {
            // TODO: Загрузка истории прогресса из репозитория или другого источника данных
            _progressHistory.value = createMockProgressHistory()
            calculateAnalytics(_progressHistory.value)
        }
    }

    private fun calculateAnalytics(history: List<DailyProgress>) {
        _moodTrend.value = calculateMoodTrend(history)
        _anxietyTrend.value = calculateAnxietyTrend(history)
        _productivityTrend.value = calculateProductivityScore(history)
        _meditationConsistency.value = calculateMeditationConsistency(history)
    }

    // Вспомогательные функции для расчета трендов (перенесены из AnalyticsScreen.kt)
    private fun calculateMoodTrend(progressHistory: List<DailyProgress>): Trend {
        val averageMood = progressHistory.map { it.mood }.average()
        val recommendations = when {
            averageMood < 3 -> "Рекомендуем больше времени уделять медитациям и дыхательным упражнениям"
            averageMood < 4 -> "Попробуйте добавить больше физической активности в ваш день"
            else -> "Продолжайте поддерживать хорошее настроение"
        }
        return Trend(
            value = averageMood,
            recommendations = recommendations
        )
    }

    private fun calculateAnxietyTrend(progressHistory: List<DailyProgress>): Trend {
        val averageAnxiety = progressHistory.map { it.anxiety }.average()
        val recommendations = when {
            averageAnxiety > 4 -> "Рекомендуем больше времени уделять медитациям и дыхательным упражнениям"
            averageAnxiety > 3 -> "Попробуйте добавить больше физической активности в ваш день"
            else -> "Продолжайте поддерживать низкий уровень тревоги"
        }
        return Trend(
            value = averageAnxiety,
            recommendations = recommendations
        )
    }

    private fun calculateProductivityScore(progressHistory: List<DailyProgress>): Trend {
        val averageProductivity = progressHistory.map { it.productivity }.average()
        val recommendations = when {
            averageProductivity < 3 -> "Рекомендуем больше времени уделять медитациям и дыхательным упражнениям"
            averageProductivity < 4 -> "Попробуйте добавить больше физической активности в ваш день"
            else -> "Продолжайте поддерживать высокую продуктивность"
        }
        return Trend(
            value = averageProductivity,
            recommendations = recommendations
        )
    }

    private fun calculateMeditationConsistency(progressHistory: List<DailyProgress>): Trend {
        val averageMeditationMinutes = progressHistory.map { it.meditationMinutes }.average()
        val recommendations = when {
            averageMeditationMinutes < 10 -> "Рекомендуем увеличить время медитаций до 15-20 минут"
            averageMeditationMinutes < 20 -> "Попробуйте добавить еще 5-10 минут к вашим медитациям"
            else -> "Продолжайте поддерживать регулярную практику медитаций"
        }
        return Trend(
            value = averageMeditationMinutes,
            recommendations = recommendations
        )
    }

    // Вспомогательная функция для создания тестовых данных (перенесена из AnalyticsScreen.kt)
    private fun createMockProgressHistory(): List<DailyProgress> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return listOf(
            DailyProgress(
                date = LocalDate.parse("2024-03-01", formatter),
                mood = 4,
                anxiety = 3,
                productivity = 4,
                meditationMinutes = 15,
                tasksCompleted = 3
            ),
            DailyProgress(
                date = LocalDate.parse("2024-03-02", formatter),
                mood = 3,
                anxiety = 4,
                productivity = 3,
                meditationMinutes = 10,
                tasksCompleted = 2
            ),
            DailyProgress(
                date = LocalDate.parse("2024-03-03", formatter),
                mood = 5,
                anxiety = 2,
                productivity = 5,
                meditationMinutes = 20,
                tasksCompleted = 4
            )
        )
    }
} 