package com.anima.service

import com.anima.data.*
import java.time.LocalDateTime

class TestService {
    fun getPHQ9Test(): Test {
        return Test(
            id = "phq9",
            title = "Тест PHQ-9",
            description = "Опросник для оценки депрессии",
            questions = listOf(
                TestQuestion(
                    id = "phq9_1",
                    text = "Как часто за последние 2 недели вас беспокоили следующие проблемы: " +
                            "Снижение интереса или удовольствия от деятельности",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_2",
                    text = "Подавленное настроение, тоска или безнадежность",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_3",
                    text = "Проблемы с засыпанием, прерывистый сон или сонливость",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_4",
                    text = "Чувство усталости или упадок сил",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_5",
                    text = "Плохой аппетит или переедание",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_6",
                    text = "Плохое мнение о себе, ощущение себя неудачником",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_7",
                    text = "Трудности с концентрацией внимания",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_8",
                    text = "Замедленность движений/речи или, наоборот, двигательное беспокойство",
                    options = getPHQ9Options()
                ),
                TestQuestion(
                    id = "phq9_9",
                    text = "Мысли о самоповреждении или о том, что лучше было бы умереть",
                    options = getPHQ9Options()
                )
            )
        )
    }

    fun getGAD7Test(): Test {
        return Test(
            id = "gad7",
            title = "Тест GAD-7",
            description = "Опросник для оценки тревожности",
            questions = listOf(
                TestQuestion(
                    id = "gad7_1",
                    text = "Как часто за последние 2 недели вас беспокоили следующие проблемы: " +
                            "Чувство нервозности, тревоги или напряжения",
                    options = getGAD7Options()
                ),
                TestQuestion(
                    id = "gad7_2",
                    text = "Неспособность остановить или контролировать беспокойство",
                    options = getGAD7Options()
                ),
                TestQuestion(
                    id = "gad7_3",
                    text = "Слишком сильное беспокойство о разных вещах",
                    options = getGAD7Options()
                ),
                TestQuestion(
                    id = "gad7_4",
                    text = "Трудности с расслаблением",
                    options = getGAD7Options()
                ),
                TestQuestion(
                    id = "gad7_5",
                    text = "Такое сильное беспокойство, что трудно усидеть на месте",
                    options = getGAD7Options()
                ),
                TestQuestion(
                    id = "gad7_6",
                    text = "Легко раздражаетесь или становитесь вспыльчивым",
                    options = getGAD7Options()
                ),
                TestQuestion(
                    id = "gad7_7",
                    text = "Испытываете страх, как будто может случиться что-то ужасное",
                    options = getGAD7Options()
                )
            )
        )
    }

    fun calculatePHQ9Score(answers: Map<String, Int>): TestResult {
        val totalScore = answers.values.sum()
        val severity = when {
            totalScore <= 4 -> "Минимальная"
            totalScore <= 9 -> "Легкая"
            totalScore <= 14 -> "Умеренная"
            totalScore <= 19 -> "Умеренно тяжелая"
            else -> "Тяжелая"
        }

        val recommendations = when (severity) {
            "Минимальная" -> listOf(
                "Продолжайте практиковать техники саморегуляции",
                "Ведите дневник настроения"
            )
            "Легкая" -> listOf(
                "Увеличьте время медитации",
                "Практикуйте техники глубокого дыхания",
                "Ведите дневник настроения"
            )
            "Умеренная" -> listOf(
                "Рекомендуется консультация специалиста",
                "Увеличьте физическую активность",
                "Практикуйте техники осознанности"
            )
            "Умеренно тяжелая", "Тяжелая" -> listOf(
                "Необходима консультация специалиста",
                "Рассмотрите возможность психотерапии",
                "Практикуйте техники самопомощи"
            )
            else -> emptyList()
        }

        return TestResult(
            testId = "phq9",
            score = totalScore,
            severity = severity,
            recommendations = recommendations,
            date = LocalDateTime.now()
        )
    }

    fun calculateGAD7Score(answers: Map<String, Int>): TestResult {
        val totalScore = answers.values.sum()
        val severity = when {
            totalScore <= 4 -> "Минимальная"
            totalScore <= 9 -> "Легкая"
            totalScore <= 14 -> "Умеренная"
            else -> "Тяжелая"
        }

        val recommendations = when (severity) {
            "Минимальная" -> listOf(
                "Продолжайте практиковать техники релаксации",
                "Ведите дневник тревожности"
            )
            "Легкая" -> listOf(
                "Практикуйте техники глубокого дыхания",
                "Используйте приложение для медитации",
                "Ведите дневник тревожности"
            )
            "Умеренная" -> listOf(
                "Рекомендуется консультация специалиста",
                "Практикуйте техники осознанности",
                "Используйте приложение для медитации"
            )
            "Тяжелая" -> listOf(
                "Необходима консультация специалиста",
                "Рассмотрите возможность психотерапии",
                "Практикуйте техники самопомощи"
            )
            else -> emptyList()
        }

        return TestResult(
            testId = "gad7",
            score = totalScore,
            severity = severity,
            recommendations = recommendations,
            date = LocalDateTime.now()
        )
    }

    private fun getPHQ9Options(): List<TestOption> {
        return listOf(
            TestOption(0, "Совсем нет"),
            TestOption(1, "Несколько дней"),
            TestOption(2, "Более половины дней"),
            TestOption(3, "Почти каждый день")
        )
    }

    private fun getGAD7Options(): List<TestOption> {
        return listOf(
            TestOption(0, "Совсем нет"),
            TestOption(1, "Несколько дней"),
            TestOption(2, "Более половины дней"),
            TestOption(3, "Почти каждый день")
        )
    }
}

data class Test(
    val id: String,
    val title: String,
    val description: String,
    val questions: List<TestQuestion>
)

data class TestQuestion(
    val id: String,
    val text: String,
    val options: List<TestOption>
)

data class TestOption(
    val score: Int,
    val text: String
)

data class TestResult(
    val testId: String,
    val score: Int,
    val severity: String,
    val recommendations: List<String>,
    val date: LocalDateTime
) 