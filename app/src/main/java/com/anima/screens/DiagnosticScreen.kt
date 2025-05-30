package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.data.diagnostic.*
import com.anima.viewmodel.DiagnosticViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticScreen(
    viewModel: DiagnosticViewModel = viewModel()
) {
    val availableTests by viewModel.availableTests.collectAsState()
    val currentTest by viewModel.currentTest.collectAsState()
    val testProgress by viewModel.testProgress.collectAsState()
    val testResults by viewModel.testResults.collectAsState()
    val isTestInProgress by viewModel.isTestInProgress.collectAsState()
    val answers by viewModel.answers.collectAsState()
    val testHistory by viewModel.testHistory.collectAsState()
    val comparativeAnalysis by viewModel.comparativeAnalysis.collectAsState()
    val detailedBreakdown by viewModel.detailedBreakdown.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isTestInProgress) {
            TestInProgress(
                currentTest = currentTest,
                progress = testProgress,
                answers = answers,
                onAnswer = { questionId, answer -> viewModel.answerQuestion(questionId, answer) },
                onSubmit = { viewModel.submitTest() }
            )
        } else if (testResults != null) {
            TestResults(
                results = testResults!!,
                comparativeAnalysis = comparativeAnalysis,
                detailedBreakdown = detailedBreakdown,
                onReset = { viewModel.resetTest() }
            )
        } else {
            AvailableTests(
                tests = availableTests,
                onTestSelected = { testId -> viewModel.startTest(testId) }
            )
        }
    }
}

@Composable
private fun TestInProgress(
    currentTest: DiagnosticTest?,
    progress: Float,
    answers: Map<String, Int>,
    onAnswer: (String, Int) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Прогресс-бар
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Вопросы теста
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            currentTest?.questions?.forEach { question ->
                item {
                    QuestionCard(
                        question = question,
                        selectedAnswer = answers[question.id],
                        onAnswerSelected = { answer -> onAnswer(question.id, answer) }
                    )
                }
            }
        }

        // Кнопка отправки
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = answers.size == currentTest?.questions?.size
        ) {
            Text("Завершить тест")
        }
    }
}

@Composable
private fun QuestionCard(
    question: DiagnosticQuestion,
    selectedAnswer: Int?,
    onAnswerSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            question.options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedAnswer == option.value,
                        onClick = { onAnswerSelected(option.value) }
                    )
                    Text(
                        text = option.text,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TestResults(
    results: DiagnosticResult,
    comparativeAnalysis: ComparativeAnalysis?,
    detailedBreakdown: Map<String, DetailedScore>,
    onReset: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Общие результаты
        item {
            ResultCard(
                title = "Общие результаты",
                content = {
                    Column {
                        Text("Интерпретация: ${results.interpretation}")
                        Text("Уровень риска: ${results.riskLevel}")
                    }
                }
            )
        }

        // Рекомендации
        item {
            ResultCard(
                title = "Рекомендации",
                content = {
                    Column {
                        results.recommendations.forEach { recommendation ->
                            Text("• $recommendation")
                        }
                    }
                }
            )
        }

        // Сравнительный анализ
        comparativeAnalysis?.let { analysis ->
            item {
                ResultCard(
                    title = "Сравнительный анализ",
                    content = {
                        Column {
                            Text("Среднее по популяции: ${analysis.populationAverage}")
                            Text("Процентиль: ${analysis.percentile}")
                        }
                    }
                )
            }
        }

        // Детальный анализ
        item {
            ResultCard(
                title = "Детальный анализ",
                content = {
                    Column {
                        detailedBreakdown.forEach { (metric, score) ->
                            Text("$metric: ${score.interpretation}")
                        }
                    }
                }
            )
        }

        // Тренды
        if (results.trends.isNotEmpty()) {
            item {
                ResultCard(
                    title = "Тренды",
                    content = {
                        Column {
                            results.trends.forEach { trend ->
                                Text("${trend.metric}: ${trend.significance}")
                            }
                        }
                    }
                )
            }
        }

        // Кнопка сброса
        item {
            Button(
                onClick = onReset,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Начать новый тест")
            }
        }
    }
}

@Composable
private fun ResultCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun AvailableTests(
    tests: List<DiagnosticTest>,
    onTestSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(tests) { test ->
            TestCard(
                test = test,
                onClick = { onTestSelected(test.id) }
            )
        }
    }
}

@Composable
private fun TestCard(
    test: DiagnosticTest,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = test.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = test.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 