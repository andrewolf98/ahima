package com.anima.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.diagnostic.*
import com.anima.data.DiagnosticEventType
import com.anima.data.UserState
import com.anima.service.DiagnosticService
import com.anima.service.AnalyticsService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

// TODO: Для создания экземпляра этого ViewModel потребуется ViewModelProvider.Factory, который предоставит DiagnosticService и AnalyticsService из AppContainer.
// Пример:
// class DiagnosticViewModelFactory(private val diagnosticService: DiagnosticService, private val analyticsService: AnalyticsService) : ViewModelProvider.Factory {
//     override fun <T : ViewModel> create(modelClass: Class<T>): T {
//         if (modelClass.isAssignableFrom(DiagnosticViewModel::class.java)) {
//             @Suppress("UNCHECKED_CAST")
//             return DiagnosticViewModel(diagnosticService, analyticsService) as T
//         }
//         throw IllegalArgumentException("Unknown ViewModel class")
//     }
// }
// В Activity/Fragment использовать:
// val viewModel: DiagnosticViewModel by viewModels { (application as AnimaApplication).appContainer.diagnosticViewModelFactory }

class DiagnosticViewModel(
    private val diagnosticService: DiagnosticService,
    private val analyticsService: AnalyticsService // Теперь зависимость передается через конструктор
) : AndroidViewModel(Application()) {

    private val _availableTests = MutableStateFlow<List<DiagnosticTest>>(emptyList())
    val availableTests: StateFlow<List<DiagnosticTest>> = _availableTests.asStateFlow()

    private val _currentTest = MutableStateFlow<DiagnosticTest?>(null)
    val currentTest: StateFlow<DiagnosticTest?> = _currentTest.asStateFlow()

    private val _testProgress = MutableStateFlow(0f)
    val testProgress: StateFlow<Float> = _testProgress.asStateFlow()

    private val _testResults = MutableStateFlow<DiagnosticResult?>(null)
    val testResults: StateFlow<DiagnosticResult?> = _testResults.asStateFlow()

    private val _answers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val answers: StateFlow<Map<String, Int>> = _answers.asStateFlow()

    private val _isTestInProgress = MutableStateFlow(false)
    val isTestInProgress: StateFlow<Boolean> = _isTestInProgress.asStateFlow()

    private val _testHistory = MutableStateFlow<List<DiagnosticResult>>(emptyList())
    val testHistory: StateFlow<List<DiagnosticResult>> = _testHistory.asStateFlow()

    private val _comparativeAnalysis = MutableStateFlow<ComparativeAnalysis?>(null)
    val comparativeAnalysis: StateFlow<ComparativeAnalysis?> = _comparativeAnalysis.asStateFlow()

    private val _detailedBreakdown = MutableStateFlow<Map<String, DetailedScore>>(emptyMap())
    val detailedBreakdown: StateFlow<Map<String, DetailedScore>> = _detailedBreakdown.asStateFlow()

    init {
        loadAvailableTests()
        loadTestHistory()
    }

    private fun loadAvailableTests() {
        viewModelScope.launch {
            _availableTests.value = diagnosticService.getAvailableTests()
        }
    }

    private fun loadTestHistory() {
        viewModelScope.launch {
            // TODO: Загрузить историю тестов из базы данных
            _testHistory.value = emptyList()
        }
    }

    fun startTest(testId: String) {
        viewModelScope.launch {
            _isTestInProgress.value = true
            _currentTest.value = _availableTests.value.find { it.id == testId }
            _answers.value = emptyMap()
            _testProgress.value = 0f
            _testResults.value = null
            _comparativeAnalysis.value = null
            _detailedBreakdown.value = emptyMap()

            // Логируем начало теста
            analyticsService.logDiagnosticEvent(
                DiagnosticEvent(
                    userId = "current_user_id", // TODO: Получить ID текущего пользователя
                    testId = testId,
                    date = LocalDateTime.now(),
                    eventType = DiagnosticEventType.TEST_STARTED
                )
            )
        }
    }

    fun answerQuestion(questionId: String, answer: Int) {
        viewModelScope.launch {
            val currentAnswers = _answers.value.toMutableMap()
            currentAnswers[questionId] = answer
            _answers.value = currentAnswers

            // Логируем ответ на вопрос
            _currentTest.value?.let { test ->
                 analyticsService.logDiagnosticEvent(
                    DiagnosticEvent(
                        userId = "current_user_id", // TODO: Получить ID текущего пользователя
                        testId = test.id,
                        date = LocalDateTime.now(),
                        eventType = DiagnosticEventType.QUESTION_ANSWERED,
                        questionId = questionId,
                        answer = answer
                    )
                )
            }

            // Обновляем прогресс
            _currentTest.value?.let { test ->
                val totalQuestions = test.questions.size
                val answeredQuestions = currentAnswers.size
                _testProgress.value = answeredQuestions.toFloat() / totalQuestions
            }
        }
    }

    fun submitTest() {
        viewModelScope.launch {
            _currentTest.value?.let { test ->
                val result = diagnosticService.processTestResults(
                    testId = test.id,
                    userId = "current_user_id", // TODO: Получить ID текущего пользователя
                    answers = _answers.value
                )
                _testResults.value = result
                _comparativeAnalysis.value = result.comparativeAnalysis
                _detailedBreakdown.value = result.detailedBreakdown
                _isTestInProgress.value = false

                // Обновляем историю тестов
                val updatedHistory = _testHistory.value.toMutableList()
                updatedHistory.add(result)
                _testHistory.value = updatedHistory

                // Логируем завершение теста
                analyticsService.logDiagnosticEvent(
                    DiagnosticEvent(
                        userId = "current_user_id", // TODO: Получить ID текущего пользователя
                        testId = test.id,
                        date = LocalDateTime.now(),
                        eventType = DiagnosticEventType.TEST_COMPLETED,
                        resultId = result.testId // Используем testId как resultId пока нет уникального ID результата
                    )
                )
            }
        }
    }

    fun resetTest() {
        viewModelScope.launch {
            _currentTest.value = null
            _answers.value = emptyMap()
            _testProgress.value = 0f
            _testResults.value = null
            _comparativeAnalysis.value = null
            _detailedBreakdown.value = emptyMap()
            _isTestInProgress.value = false
        }
    }

    fun getRecommendations(): List<String> {
        return _testResults.value?.recommendations ?: emptyList()
    }

    fun getRiskLevel(): RiskLevel {
        return _testResults.value?.riskLevel ?: RiskLevel.LOW
    }

    fun getInterpretation(): String {
        return _testResults.value?.interpretation ?: ""
    }

    fun getScores(): Map<String, Int> {
        return _testResults.value?.scores ?: emptyMap()
    }

    fun getTrends(): List<DiagnosticTrend> {
        return _testResults.value?.trends ?: emptyList()
    }

    fun getDetailedScore(metric: String): DetailedScore? {
        return _detailedBreakdown.value[metric]
    }

    fun getTestHistory(testId: String): List<DiagnosticResult> {
        return _testHistory.value.filter { it.testId == testId }
    }

    fun getComparativeAnalysis(): ComparativeAnalysis? {
        return _comparativeAnalysis.value
    }
} 