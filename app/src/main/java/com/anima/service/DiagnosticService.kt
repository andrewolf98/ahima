package com.anima.service

import com.anima.data.diagnostic.*
import java.time.LocalDateTime

class DiagnosticService {
    // Получение доступных тестов
    fun getAvailableTests(): List<DiagnosticTest> {
        return listOf(
            getPCL5Test(),
            getBeckDepressionTest(),
            getCognitiveDistortionTest(),
            getQualityOfLifeTest(),
            getSleepTest(),
            getStressTest(),
            getSocialTest()
        )
    }

    // PCL-5 тест (Шкала ПТСР)
    private fun getPCL5Test(): DiagnosticTest {
        return DiagnosticTest(
            id = "pcl5",
            title = "PCL-5 (Шкала ПТСР)",
            description = "Оценка симптомов посттравматического стрессового расстройства за последний месяц",
            category = TestCategory.PTSD,
            questions = getPCL5Questions(),
            scoringSystem = ScoringSystem(
                minScore = 0,
                maxScore = 80, // 20 вопросов * 4 балла (макс по симптому) = 80
                thresholds = listOf(
                    ScoreThreshold(0, 31, "Минимальный", listOf(
                        "Продолжайте практиковать техники саморегуляции и следите за своим состоянием.",
                        "Ведите дневник эмоций для лучшего понимания себя."
                    )),
                    ScoreThreshold(32, 37, "Умеренный", listOf(
                        "Рекомендуется консультация специалиста для оценки состояния.",
                        "Используйте техники заземления и дыхательные упражнения при тревоге.",
                        "Рассмотрите возможность использования медитаций и релаксационных техник."
                    )),
                    ScoreThreshold(38, 80, "Высокий", listOf(
                        "Необходима консультация специалиста, специализирующегося на травме (психотерапевт, психиатр).",
                        "Рассмотрите возможность прохождения специализированной психотерапии (например, КПТ, ДПДГ).",
                        "Обратитесь за поддержкой к близким и в специализированные группы."
                    ))
                ),
                interpretation = mapOf(
                    "total" to "Общий балл PCL-5 указывает на общую выраженность симптомов ПТСР.",
                    PCL5Cluster.INTRUSION.name.toLowerCase() to "Симптомы вторжения (навязчивые воспоминания, кошмары, флешбэки, дистресс при напоминаниях).",
                    PCL5Cluster.AVOIDANCE.name.toLowerCase() to "Симптомы избегания (избегание мыслей, чувств, внешних напоминаний о травме).",
                    PCL5Cluster.NEGATIVE_ALTERATIONS.name.toLowerCase() to "Негативные изменения в мышлении и настроении (негативные убеждения, потеря интереса, отстраненность, трудности с позитивными эмоциями).",
                    PCL5Cluster.AROUSAL.name.toLowerCase() to "Изменения в возбуждении и реактивности (раздражительность, безрассудство, настороженность, трудности с концентрацией/сном)."
                )
            )
        )
    }

    private fun getPCL5Questions(): List<DiagnosticQuestion> {
        val options = getPCL5Options()
        return listOf(
            // Кластер B: Симптомы вторжения (5 вопросов)
            DiagnosticQuestion(
                id = "pcl5_b_1",
                text = "Повторяющиеся, непроизвольные и тревожные воспоминания о травматическом событии.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "pcl5_b_2",
                text = "Повторяющиеся кошмарные сны о травматическом событии.",
                options = options,
                category = QuestionCategory.SLEEP
            ),
            DiagnosticQuestion(
                id = "pcl5_b_3",
                text = "Чувство, будто травматическое событие происходит прямо сейчас (вспышки воспоминаний, иллюзии, галлюцинации) - диссоциативные реакции.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "pcl5_b_4",
                text = "Сильное психологическое расстройство при воздействии внутренних или внешних напоминаний о травматическом событии.",
                options = options,
                category = QuestionCategory.MOOD
            ),
            DiagnosticQuestion(
                id = "pcl5_b_5",
                text = "Выраженные физиологические реакции при воздействии внутренних или внешних напоминаний о травматическом событии (учащенное сердцебиение, потливость, дрожь).",
                options = options,
                category = QuestionCategory.PHYSICAL
            ),

            // Кластер C: Симптомы избегания (2 вопроса)
            DiagnosticQuestion(
                id = "pcl5_c_1",
                text = "Избегание тревожных мыслей, чувств или разговоров, связанных с травматическим событием.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "pcl5_c_2",
                text = "Избегание внешних напоминаний о травматическом событии (людей, мест, занятий, ситуаций).",
                options = options,
                category = QuestionCategory.SOCIAL
            ),

            // Кластер D: Негативные изменения в мышлении и настроении (7 вопросов)
            DiagnosticQuestion(
                id = "pcl5_d_1",
                text = "Неспособность вспомнить важный аспект травматического события (амнезия диссоциативного характера).",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "pcl5_d_2",
                text = "Стойкие и искаженные негативные убеждения о себе, других или мире (например, 'Я плохой', 'Миру нельзя доверять').",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "pcl5_d_3",
                text = "Стойкие искаженные представления о причине или последствиях травматического события, которые приводят к самообвинению или обвинению других.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "pcl5_d_4",
                text = "Стойкое негативное эмоциональное состояние (страх, ужас, гнев, вина, стыд).",
                options = options,
                category = QuestionCategory.MOOD
            ),
             DiagnosticQuestion(
                id = "pcl5_d_5",
                text = "Заметное снижение интереса или участия в значимых занятиях.",
                options = options,
                category = QuestionCategory.SOCIAL // Связано с поведением/активностью
            ),
             DiagnosticQuestion(
                id = "pcl5_d_6",
                text = "Чувство отстраненности или отчужденности от других людей.",
                options = options,
                category = QuestionCategory.SOCIAL
            ),
             DiagnosticQuestion(
                id = "pcl5_d_7",
                text = "Стойкая неспособность испытывать положительные эмоции (счастье, удовлетворение, любовь).",
                options = options,
                category = QuestionCategory.MOOD
            ),

            // Кластер E: Изменения в возбуждении и реактивности (6 вопросов)
            DiagnosticQuestion(
                id = "pcl5_e_1",
                text = "Раздражительность и гневные вспышки, которые проявляются вербальной или физической агрессией по отношению к людям или предметам.",
                options = options,
                category = QuestionCategory.MOOD // Связано с регуляцией эмоций
            ),
            DiagnosticQuestion(
                id = "pcl5_e_2",
                text = "Безрассудное или саморазрушительное поведение.",
                options = options,
                category = QuestionCategory.SOCIAL // Связано с импульсивным поведением
            ),
            DiagnosticQuestion(
                id = "pcl5_e_3",
                text = "Повышенная настороженность.",
                options = options,
                category = QuestionCategory.ANXIETY // Связано с бдительностью
            ),
            DiagnosticQuestion(
                id = "pcl5_e_4",
                text = "Проблемы с концентрацией внимания.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "pcl5_e_5",
                text = "Нарушения сна (трудности с засыпанием или поддержанием сна).",
                options = options,
                category = QuestionCategory.SLEEP
            ),
             DiagnosticQuestion(
                id = "pcl5_e_6",
                text = "Чрезмерная реакция испуга (вздрагивание).",
                options = options,
                category = QuestionCategory.PHYSICAL // Связано с физической реактивностью
            ),
        )
    }

    private fun getPCL5Options(): List<QuestionOption> {
        return listOf(
            QuestionOption(0, "Совсем нет", "Симптом отсутствовал за последний месяц"),
            QuestionOption(1, "Слабо", "Симптом присутствовал 1-2 раза за последний месяц"),
            QuestionOption(2, "Умеренно", "Симптом присутствовал несколько раз в неделю за последний месяц"),
            QuestionOption(3, "Сильно", "Симптом присутствовал 3-5 раз в неделю за последний месяц"),
            QuestionOption(4, "Очень сильно", "Симптом присутствовал почти каждый день за последний месяц")
        )
    }

    // Шкала депрессии Бека (BDI-II) - 21 вопрос
    private fun getBeckDepressionTest(): DiagnosticTest {
        return DiagnosticTest(
            id = "beck",
            title = "Шкала депрессии Бека (BDI-II)",
            description = "Оценка тяжести депрессивных симптомов за последние 2 недели",
            category = TestCategory.DEPRESSION,
            questions = getBeckQuestions(),
            scoringSystem = ScoringSystem(
                minScore = 0,
                maxScore = 63, // 21 вопрос * 3 балла = 63
                thresholds = listOf(
                    ScoreThreshold(0, 13, "Минимальная", listOf(
                        "Продолжайте поддерживать свое эмоциональное состояние и используйте приложение для поддержания благополучия.",
                        "Используйте техники релаксации и осознанности при необходимости."
                    )),
                    ScoreThreshold(14, 19, "Легкая", listOf(
                        "Рекомендуется обратить внимание на свое состояние. Попробуйте увеличить физическую активность и социальную активность.",
                        "Ведите дневник настроения и отслеживайте триггеры.",
                        "Рассмотрите возможность консультации с психологом."
                    )),
                    ScoreThreshold(20, 28, "Умеренная", listOf(
                        "Рекомендуется консультация специалиста (психотерапевта или психиатра) для оценки и составления плана помощи.",
                        "Практикуйте техники осознанности и самопомощи регулярно.",
                        "Используйте поддержку близких и, если возможно, присоединитесь к группе поддержки."
                    )),
                    ScoreThreshold(29, 63, "Тяжелая", listOf(
                        "Срочно необходима консультация специалиста (психиатра).",
                        "Рассмотрите возможность психотерапии и, возможно, медикаментозного лечения.",
                        "Не оставайтесь наедине со своими переживаниями. Обратитесь за помощью к близким или в службы экстренной психологической помощи."
                    ))
                ),
                interpretation = mapOf(
                    "total" to "Общий балл шкалы Бека указывает на общую тяжесть депрессивных симптомов.",
                    "sadness" to "Грусть.",
                    "pessimism" to "Пессимизм.",
                    "past_failure" to "Ощущение неудач в прошлом.",
                    "loss_of_pleasure" to "Потеря удовольствия.",
                    "guilty_feelings" to "Чувство вины.",
                    "punishment_feelings" to "Чувство наказания.",
                    "self_dislike" to "Недовольство собой.",
                    "self_blame" to "Самообвинение.",
                    "suicidal_thoughts" to "Суицидальные мысли.",
                    "crying" to "Плаксивость.",
                    "agitation" to "Возбуждение или заторможенность.",
                    "loss_of_interest" to "Потеря интереса к людям/событиям.",
                    "indecisiveness" to "Нерешительность.",
                    "worthlessness" to "Ощущение никчемности.",
                    "loss_of_energy" to "Потеря энергии.",
                    "changes_in_sleep" to "Изменения сна.",
                    "irritability" to "Раздражительность.",
                    "changes_in_appetite" to "Изменения аппетита.",
                    "weight_change" to "Изменения веса.",
                    "body_preoccupation" to "Озабоченность своим здоровьем.",
                    "loss_of_libido" to "Потеря интереса к сексу."
                )
            )
        )
    }

    private fun getBeckQuestions(): List<DiagnosticQuestion> {
        val options = getBeckOptions()
        return listOf(
            DiagnosticQuestion(
                id = "beck_1",
                text = "1. Грусть: 0 - Я не грущу. 1 - Я грущу. 2 - Я все время грущу и не могу от этого избавиться. 3 - Мне так грустно или несчастно, что я не могу это вынести.",
                options = options,
                category = QuestionCategory.MOOD
            ),
            DiagnosticQuestion(
                id = "beck_2",
                text = "2. Пессимизм: 0 - Я не испытываю особого разочарования по поводу будущего. 1 - Я разочарован будущим. 2 - Я чувствую, что мне нечего ждать в будущем. 3 - Я чувствую, что у будущего нет надежды и оно не может улучшиться.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "beck_3",
                text = "3. Неудачи в прошлом: 0 - Я не чувствую себя неудачником. 1 - Я считаю, что терпел больше неудач, чем среднестатистический человек. 2 - Когда я смотрю назад, я вижу много неудач. 3 - Я чувствую себя полным неудачником.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            DiagnosticQuestion(
                id = "beck_4",
                text = "4. Потеря удовольствия: 0 - Мне все так же нравится то, что раньше приносило удовольствие. 1 - Мне стало не так интересно, как раньше. 2 - Мне почти ничего не приносит удовольствия. 3 - Я совсем не могу получить удовольствие от чего-либо.",
                options = options,
                category = QuestionCategory.MOOD
            ),
             DiagnosticQuestion(
                id = "beck_5",
                text = "5. Чувство вины: 0 - Я не чувствую себя особенно виноватым. 1 - Я чувствую себя виноватым по отношению ко многим вещам, которые я сделал или не сделал. 2 - Я чувствую себя виноватым почти все время. 3 - Я чувствую себя совершенно бесполезным и очень виноватым.",
                options = options,
                category = QuestionCategory.MOOD
            ),
             DiagnosticQuestion(
                id = "beck_6",
                text = "6. Чувство наказания: 0 - Я не чувствую, что меня наказывают. 1 - Я чувствую, что меня, возможно, наказывают. 2 - Я ожидаю наказания. 3 - Я чувствую, что меня наказывают.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "beck_7",
                text = "7. Недовольство собой: 0 - Я отношусь к себе так же, как и всегда. 1 - Я потерял уверенность в себе. 2 - Я разочарован собой. 3 - Я ненавижу себя.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "beck_8",
                text = "8. Самообвинение: 0 - Я не виню себя больше, чем обычно. 1 - Я виню себя за свои ошибки. 2 - Я постоянно виню себя за свои недостатки. 3 - Я виню себя за все плохое, что происходит.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "beck_9",
                text = "9. Суицидальные мысли: 0 - У меня нет никаких мыслей о самоубийстве. 1 - У меня есть мысли о самоубийстве, но я не буду их реализовывать. 2 - Я хотел бы покончить с собой. 3 - Я бы убил себя, если бы у меня была возможность.",
                options = options,
                category = QuestionCategory.MOOD
            ),
             DiagnosticQuestion(
                id = "beck_10",
                text = "10. Плаксивость: 0 - Я не плачу больше, чем обычно. 1 - Я плачу больше, чем раньше. 2 - Я плачу все время сейчас. 3 - Раньше я мог плакать, но теперь я не могу плакать, даже если хочу.",
                options = options,
                category = QuestionCategory.MOOD
            ),
            // TODO: Добавить остальные вопросы шкалы Бека (BDI-II) - всего 21
             // 11. Раздражительность, 12. Потеря интереса, 13. Нерешительность, 14. Никчемность,
             // 15. Потеря энергии, 16. Изменения сна, 17. Раздражительность,
             // 18. Изменения аппетита, 19. Изменения веса, 20. Озабоченность телом, 21. Потеря либидо
        )
    }

    private fun getBeckOptions(): List<QuestionOption> {
        // Опции для шкалы Бека - текст каждого варианта интегрирован в текст вопроса
        // Здесь будут только значения баллов от 0 до 3
        return listOf(
            QuestionOption(0, "0 баллов", null),
            QuestionOption(1, "1 балл", null),
            QuestionOption(2, "2 балла", null),
            QuestionOption(3, "3 балла", null)
        )
    }

     // Тест когнитивных искажений (примерная структура)
    private fun getCognitiveDistortionTest(): DiagnosticTest {
        return DiagnosticTest(
            id = "cognitive",
            title = "Тест на выявление когнитивных искажений",
            description = "Оцените, насколько следующие утверждения характерны для вашего мышления",
            category = TestCategory.COGNITIVE,
            questions = getCognitiveDistortionQuestions(),
            scoringSystem = ScoringSystem(
                minScore = 0,
                maxScore = 50, // Примерно, зависит от количества вопросов и опций
                 thresholds = listOf(
                    ScoreThreshold(0, 10, "Низкий уровень", listOf(
                        "Ваше мышление преимущественно реалистично и сбалансировано.",
                        "Продолжайте отслеживать свои мысли и оспаривать нелогичные."
                    )),
                    ScoreThreshold(11, 25, "Средний уровень", listOf(
                        "У вас присутствуют некоторые когнитивные искажения, которые могут влиять на ваше настроение и поведение.",
                        "Изучите основные типы искажений и начните активно оспаривать их, используя методы КПТ."
                    )),
                    ScoreThreshold(26, 50, "Высокий уровень", listOf(
                        "Ваше мышление склонно к значительному количеству искажений, что может существенно влиять на ваше эмоциональное состояние.",
                        "Настоятельно рекомендуется работа с когнитивно-поведенческим терапевтом для изменения паттернов мышления.",
                        "Практикуйте техники осознанности, чтобы замечать свои мысли без осуждения."
                    ))
                ),
                interpretation = mapOf(
                    "total" to "Общий уровень выраженности когнитивных искажений. Чем выше балл, тем чаще ваше мышление искажено.",
                     DistortionType.ALL_OR_NOTHING.name.toLowerCase() to "Мышление по типу «всё или ничего» (черно-белое мышление).",
                    DistortionType.OVERGENERALIZATION.name.toLowerCase() to "Сверхобобщение (один негативный случай воспринимается как бесконечная череда поражений).",
                    DistortionType.MENTAL_FILTER.name.toLowerCase() to "Ментальный фильтр (выделение одной негативной детали и игнорирование всего позитивного).",
                    DistortionType.DISQUALIFYING_POSITIVE.name.toLowerCase() to "Обесценивание позитивного (игнорирование или отрицание положительного опыта).",
                    DistortionType.JUMPING_TO_CONCLUSIONS.name.toLowerCase() to "Поспешные выводы (чтение мыслей или предсказание будущего).",
                    DistortionType.MAGNIFICATION.name.toLowerCase() to "Преувеличение (катастрофизация) или преуменьшение.",
                    DistortionType.EMOTIONAL_REASONING.name.toLowerCase() to "Эмоциональное обоснование (чувства воспринимаются как истина: 'Я чувствую себя виноватым, значит, я виноват').",
                    DistortionType.SHOULD_STATEMENTS.name.toLowerCase() to "Утверждения с «должен» (жесткие правила для себя и других, ведущие к чувству вины или гнева).",
                    DistortionType.LABELING.name.toLowerCase() to "Навешивание ярлыков (крайняя форма сверхобобщения, присвоение себе или другим негативных ярлыков).",
                    DistortionType.PERSONALIZATION.name.toLowerCase() to "Персонализация (принятие на себя ответственности за негативные события, в которых нет вашей вины)."
                )
            )
        )
    }

    private fun getCognitiveDistortionQuestions(): List<DiagnosticQuestion> {
         val options = getCognitiveDistortionOptions()
        return listOf(
            DiagnosticQuestion(
                id = "cog_1",
                text = "Если я не справляюсь идеально с задачей, значит, я полный неудачник.",
                options = options,
                category = QuestionCategory.COGNITION // Категория мышления
            ),
             DiagnosticQuestion(
                id = "cog_2",
                text = "После одной неудачи в чем-либо я думаю, что неудачи будут преследовать меня во всех сферах жизни.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_3",
                text = "Даже если произошло много хорошего, я зацикливаюсь на одной негативной детали.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_4",
                text = "Когда кто-то хвалит меня, я думаю, что они просто вежливы или чего-то от меня хотят.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_5",
                text = "Я часто думаю, что знаю, что другие люди думают обо мне, даже не спрашивая их.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_6",
                text = "Я склонен думать о худшем сценарии развития событий, даже если вероятность мала.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_7",
                text = "Если я чувствую себя несчастным, значит, ситуация действительно плохая, потому что мои чувства — это правда.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_8",
                text = "Я считаю, что я или другие люди ДОЛЖНЫ вести себя определенным образом, и сильно расстраиваюсь, когда это не так.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_9",
                text = "Если я допустил ошибку, я думаю о себе как о полном неудачнике или идиоте.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
             DiagnosticQuestion(
                id = "cog_10",
                text = "Я думаю, что я виноват, когда что-то идет не так, даже если это не полностью моя ответственность.",
                options = options,
                category = QuestionCategory.COGNITION
            ),
        )
    }

     private fun getCognitiveDistortionOptions(): List<QuestionOption> {
         return listOf(
            QuestionOption(0, "Совсем не похоже на меня", null),
            QuestionOption(1, "Немного похоже на меня", null),
            QuestionOption(2, "Средне похоже на меня", null),
            QuestionOption(3, "Очень похоже на меня", null)
        )
     }

    // Тест оценки качества жизни (примерная структура) - 10 вопросов
    private fun getQualityOfLifeTest(): DiagnosticTest {
        return DiagnosticTest(
            id = "qol",
            title = "Оценка качества жизни (WHOQOL-BREF, адаптированный)",
            description = "Оцените, насколько вы удовлетворены различными аспектами вашей жизни за последние две недели",
            category = TestCategory.QUALITY_OF_LIFE,
            questions = getQualityOfLifeQuestions(),
            scoringSystem = ScoringSystem(
                minScore = 10,
                maxScore = 50, // 10 вопросов * 5 баллов
                 thresholds = listOf(
                    ScoreThreshold(40, 50, "Высокое качество жизни", listOf(
                        "Продолжайте поддерживать сбалансированный образ жизни и цените свои достижения.",
                        "Используйте свои сильные стороны для дальнейшего развития."
                    )),
                    ScoreThreshold(25, 39, "Среднее качество жизни", listOf(
                        "Обратите внимание на области жизни, которые получили более низкие оценки. Подумайте, что можно улучшить.",
                        "Поставьте конкретные цели для повышения удовлетворенности в этих областях."
                    )),
                    ScoreThreshold(10, 24, "Низкое качество жизни", listOf(
                        "Ваше качество жизни значительно снижено. Рекомендуется консультация специалиста (психолога, терапевта).",
                        "Поиск поддержки у близких. Изучение техник самопомощи для улучшения эмоционального состояния."
                    ))
                ),
                interpretation = mapOf(
                    "total" to "Общий балл качества жизни. Чем выше балл, тем выше удовлетворенность жизнью.",
                     LifeDomain.PHYSICAL.name.toLowerCase() to "Удовлетворенность физическим здоровьем и благополучием (энергия, отдых, подвижность, комфорт).",
                    LifeDomain.PSYCHOLOGICAL.name.toLowerCase() to "Удовлетворенность психологическим состоянием и эмоциональным благополучием (позитивные/негативные эмоции, мышление, самооценка, память/концентрация).",
                    LifeDomain.SOCIAL.name.toLowerCase() to "Удовлетворенность социальными отношениями (личные отношения, социальная поддержка, сексуальная активность).",
                    LifeDomain.ENVIRONMENTAL.name.toLowerCase() to "Удовлетворенность окружающей средой и бытовыми условиями (безопасность, быт, финансы, возможности для обучения/отдыха, экология, транспорт)."
                    // В WHOQOL-BREF нет отдельного домена Spirituality/Religion/Personal Beliefs, но в полной версии (WHOQOL-100) есть. Добавим для примера.
                    "spiritual" to "Удовлетворенность духовными убеждениями и поиском смысла."
                )
            )
        )
    }

    private fun getQualityOfLifeQuestions(): List<DiagnosticQuestion> {
         val options = getQualityOfLifeOptions()
        return listOf(
            DiagnosticQuestion(
                id = "qol_1",
                text = "Как бы Вы оценили качество Вашей жизни в целом?",
                options = getOverallQOLOptions(),
                category = QuestionCategory.COGNITION // Общая оценка
            ),
             DiagnosticQuestion(
                id = "qol_2",
                text = "Насколько Вы удовлетворены состоянием своего здоровья?",
                options = options,
                category = QuestionCategory.PHYSICAL
            ),
             DiagnosticQuestion(
                id = "qol_3",
                text = "Насколько имеющиеся у Вас боль и дискомфорт мешают Вам жить? (Оцените наоборот, насколько они *не* мешают, для соответствия шкале удовлетворенности)",
                options = getPainComfortOptions(),
                category = QuestionCategory.PHYSICAL
            ),
             DiagnosticQuestion(
                id = "qol_4",
                text = "Насколько Вы удовлетворены своим сном и отдыхом?",
                options = options,
                category = QuestionCategory.PHYSICAL
            ),
             DiagnosticQuestion(
                id = "qol_5",
                text = "Насколько Вы удовлетворены своей способностью выполнять повседневные дела?",
                options = options,
                category = QuestionCategory.PHYSICAL
            ),

             DiagnosticQuestion(
                id = "qol_6",
                text = "Насколько Вы удовлетворены собой (тем, какой Вы есть)?",
                options = options,
                category = QuestionCategory.PSYCHOLOGICAL
            ),
              DiagnosticQuestion(
                id = "qol_7",
                text = "Насколько Вы удовлетворены своими личными взаимоотношениями?",
                options = options,
                category = QuestionCategory.SOCIAL
            ),
              DiagnosticQuestion(
                id = "qol_8",
                text = "Насколько Вы удовлетворены условиями места, где Вы живете?",
                options = options,
                category = QuestionCategory.ENVIRONMENTAL
            ),
              DiagnosticQuestion(
                id = "qol_9",
                text = "Насколько Вы удовлетворены возможностью заниматься любимыми и интересными для Вас делами?",
                options = options,
                category = QuestionCategory.ENVIRONMENTAL // Связано с досугом/возможностями
            ),
             DiagnosticQuestion(
                id = "qol_10",
                text = "Насколько Вы удовлетворены своими духовными или религиозными убеждениями?",
                options = options,
                category = QuestionCategory.SOCIAL // В данном случае относим к социальному/экзистенциальному
            ),
             // TODO: Добавить остальные вопросы WHOQOL-BREF (всего 26, плюс 2 общих). Здесь адаптировано 10 для примера.
        )
    }

    private fun getQualityOfLifeOptions(): List<QuestionOption> {
         return listOf(
            QuestionOption(1, "Совсем не удовлетворен", null),
            QuestionOption(2, "Скорее не удовлетворен", null),
            QuestionOption(3, "Умеренно удовлетворен", null),
            QuestionOption(4, "Скорее удовлетворен", null),
            QuestionOption(5, "Полностью удовлетворен", null)
        )
    }

     private fun getOverallQOLOptions(): List<QuestionOption> {
         return listOf(
            QuestionOption(1, "Очень плохо", null),
            QuestionOption(2, "Плохо", null),
            QuestionOption(3, "Ни хорошо, ни плохо", null),
            QuestionOption(4, "Хорошо", null),
            QuestionOption(5, "Очень хорошо", null)
         ) // Шкала оценки качества жизни в целом
     }

     private fun getPainComfortOptions(): List<QuestionOption> {
          return listOf(
             QuestionOption(1, "Чрезвычайно мешают", null), // Оценка наоборот: 1 балл за сильное помехи
             QuestionOption(2, "Сильно мешают", null),
             QuestionOption(3, "Умеренно мешают", null),
             QuestionOption(4, "Немного мешают", null),
             QuestionOption(5, "Совсем не мешают", null) // 5 баллов за отсутствие помех
          ) // Шкала оценки влияния боли/дискомфорта
     }

    // Тест оценки качества сна (PSQI)
    private fun getSleepTest(): DiagnosticTest {
        return DiagnosticTest(
            id = "sleep",
            title = "Оценка качества сна (PSQI)",
            description = "Комплексная оценка качества сна за последний месяц",
            category = TestCategory.SLEEP,
            questions = getSleepQuestions(),
            scoringSystem = ScoringSystem(
                minScore = 0,
                maxScore = 21,
                thresholds = listOf(
                    ScoreThreshold(0, 5, "Хорошее качество сна", listOf(
                        "Продолжайте поддерживать здоровые привычки сна.",
                        "Следите за регулярностью режима сна."
                    )),
                    ScoreThreshold(6, 10, "Умеренные нарушения сна", listOf(
                        "Рекомендуется улучшить гигиену сна.",
                        "Рассмотрите возможность консультации со специалистом по сну."
                    )),
                    ScoreThreshold(11, 21, "Выраженные нарушения сна", listOf(
                        "Необходима консультация специалиста по сну.",
                        "Рассмотрите возможность когнитивно-поведенческой терапии бессонницы."
                    ))
                ),
                interpretation = mapOf(
                    "total" to "Общий балл качества сна. Чем выше балл, тем хуже качество сна.",
                    SleepDomain.SLEEP_QUALITY.name.toLowerCase() to "Субъективная оценка качества сна.",
                    SleepDomain.SLEEP_LATENCY.name.toLowerCase() to "Время засыпания.",
                    SleepDomain.SLEEP_DURATION.name.toLowerCase() to "Продолжительность сна.",
                    SleepDomain.SLEEP_EFFICIENCY.name.toLowerCase() to "Эффективность сна.",
                    SleepDomain.SLEEP_DISTURBANCES.name.toLowerCase() to "Нарушения сна.",
                    SleepDomain.SLEEP_MEDICATION.name.toLowerCase() to "Использование снотворных препаратов.",
                    SleepDomain.DAYTIME_DYSFUNCTION.name.toLowerCase() to "Дневная дисфункция."
                )
            )
        )
    }

    private fun getSleepQuestions(): List<DiagnosticQuestion> {
        val options = getSleepOptions()
        return listOf(
            DiagnosticQuestion(
                id = "sleep_1",
                text = "Как бы Вы оценили общее качество Вашего сна за последний месяц?",
                options = options,
                category = QuestionCategory.SLEEP
            ),
            DiagnosticQuestion(
                id = "sleep_2",
                text = "Сколько времени обычно проходило, прежде чем Вы засыпали каждую ночь?",
                options = getSleepLatencyOptions(),
                category = QuestionCategory.SLEEP
            ),
            DiagnosticQuestion(
                id = "sleep_3",
                text = "Сколько часов фактического сна Вы получали ночью?",
                options = getSleepDurationOptions(),
                category = QuestionCategory.SLEEP
            ),
            // Добавьте остальные вопросы PSQI
        )
    }

    private fun getSleepOptions(): List<QuestionOption> {
        return listOf(
            QuestionOption(0, "Очень хорошее", null),
            QuestionOption(1, "Довольно хорошее", null),
            QuestionOption(2, "Довольно плохое", null),
            QuestionOption(3, "Очень плохое", null)
        )
    }

    // Тест оценки уровня стресса
    private fun getStressTest(): DiagnosticTest {
        return DiagnosticTest(
            id = "stress",
            title = "Оценка уровня стресса (PSS)",
            description = "Оценка воспринимаемого уровня стресса за последний месяц",
            category = TestCategory.STRESS,
            questions = getStressQuestions(),
            scoringSystem = ScoringSystem(
                minScore = 0,
                maxScore = 40,
                thresholds = listOf(
                    ScoreThreshold(0, 13, "Низкий уровень стресса", listOf(
                        "Продолжайте использовать эффективные стратегии преодоления стресса.",
                        "Поддерживайте баланс между работой и отдыхом."
                    )),
                    ScoreThreshold(14, 26, "Умеренный уровень стресса", listOf(
                        "Рекомендуется освоить техники управления стрессом.",
                        "Рассмотрите возможность консультации с психологом."
                    )),
                    ScoreThreshold(27, 40, "Высокий уровень стресса", listOf(
                        "Необходима консультация специалиста для разработки плана управления стрессом.",
                        "Рассмотрите возможность психотерапии."
                    ))
                ),
                interpretation = mapOf(
                    "total" to "Общий уровень воспринимаемого стресса.",
                    StressDomain.PHYSICAL.name.toLowerCase() to "Физические проявления стресса.",
                    StressDomain.EMOTIONAL.name.toLowerCase() to "Эмоциональные проявления стресса.",
                    StressDomain.COGNITIVE.name.toLowerCase() to "Когнитивные проявления стресса.",
                    StressDomain.BEHAVIORAL.name.toLowerCase() to "Поведенческие проявления стресса.",
                    StressDomain.SOCIAL.name.toLowerCase() to "Социальные проявления стресса.",
                    StressDomain.WORK.name.toLowerCase() to "Профессиональные проявления стресса."
                )
            )
        )
    }

    private fun getStressQuestions(): List<DiagnosticQuestion> {
        val options = getStressOptions()
        return listOf(
            DiagnosticQuestion(
                id = "stress_1",
                text = "Как часто Вы чувствовали, что не можете контролировать важные вещи в своей жизни?",
                options = options,
                category = QuestionCategory.ANXIETY
            ),
            DiagnosticQuestion(
                id = "stress_2",
                text = "Как часто Вы чувствовали себя уверенно в своей способности справляться с личными проблемами?",
                options = options,
                category = QuestionCategory.COGNITION
            ),
            // Добавьте остальные вопросы PSS
        )
    }

    private fun getStressOptions(): List<QuestionOption> {
        return listOf(
            QuestionOption(0, "Никогда", null),
            QuestionOption(1, "Почти никогда", null),
            QuestionOption(2, "Иногда", null),
            QuestionOption(3, "Довольно часто", null),
            QuestionOption(4, "Очень часто", null)
        )
    }

    // Обработка результатов теста
    fun processTestResults(
        testId: String,
        userId: String,
        answers: Map<String, Int>
    ): DiagnosticResult {
        val test = getAvailableTests().find { it.id == testId }
        if (test == null || answers.size != test.questions.size) {
            return DiagnosticResult(
                testId = testId,
                userId = userId,
                date = LocalDateTime.now(),
                scores = emptyMap(),
                interpretation = "Ошибка: Не удалось обработать результаты теста.",
                recommendations = emptyList(),
                riskLevel = RiskLevel.SEVERE
            )
        }

        val scores = calculateScores(testId, answers)
        val interpretation = interpretScores(testId, scores)
        val recommendations = generateRecommendations(testId, scores)
        val riskLevel = determineRiskLevel(testId, scores)
        
        // Добавляем анализ трендов
        val trends = calculateTrends(testId, userId, scores)
        
        // Добавляем сравнительный анализ
        val comparativeAnalysis = generateComparativeAnalysis(testId, scores)
        
        // Добавляем детальный анализ результатов
        val detailedBreakdown = generateDetailedBreakdown(testId, scores)

        return DiagnosticResult(
            testId = testId,
            userId = userId,
            date = LocalDateTime.now(),
            scores = scores,
            interpretation = interpretation,
            recommendations = recommendations,
            riskLevel = riskLevel,
            trends = trends,
            comparativeAnalysis = comparativeAnalysis,
            detailedBreakdown = detailedBreakdown
        )
    }

    private fun calculateScores(testId: String, answers: Map<String, Int>): Map<String, Int> {
        val test = getAvailableTests().find { it.id == testId }
        // Проверка уже выполнена в processTestResults, но для надежности можно оставить или убрать
         if (test == null) return emptyMap()

        return when (testId) {
            "pcl5" -> calculatePCL5Scores(answers, test.questions.filterIsInstance<PCL5Question>(), test.scoringSystem)
            "beck" -> calculateBeckScores(answers, test.questions.filterIsInstance<BeckQuestion>(), test.scoringSystem)
            "cognitive" -> calculateCognitiveScores(answers, test.questions.filterIsInstance<CognitiveQuestion>(), test.scoringSystem)
            "qol" -> calculateQOLScores(answers, test.questions.filterIsInstance<QualityOfLifeQuestion>(), test.scoringSystem)
            "sleep" -> calculateSleepScores(answers, test.questions.filterIsInstance<SleepQuestion>(), test.scoringSystem)
            "stress" -> calculateStressScores(answers, test.questions.filterIsInstance<StressQuestion>(), test.scoringSystem)
            else -> emptyMap()
        }
    }

    private fun calculatePCL5Scores(answers: Map<String, Int>, questions: List<PCL5Question>, scoringSystem: ScoringSystem): Map<String, Int> {
        val intrusionScore = calculateClusterScore(answers, questions, PCL5Cluster.INTRUSION)
        val avoidanceScore = calculateClusterScore(answers, questions, PCL5Cluster.AVOIDANCE)
        val negativeAlterationsScore = calculateClusterScore(answers, questions, PCL5Cluster.NEGATIVE_ALTERATIONS)
        val arousalScore = calculateClusterScore(answers, questions, PCL5Cluster.AROUSAL)
        val totalScore = intrusionScore + avoidanceScore + negativeAlterationsScore + arousalScore

        return mapOf(
            "total" to totalScore,
            PCL5Cluster.INTRUSION.name.toLowerCase() to intrusionScore,
            PCL5Cluster.AVOIDANCE.name.toLowerCase() to avoidanceScore,
            PCL5Cluster.NEGATIVE_ALTERATIONS.name.toLowerCase() to negativeAlterationsScore,
            PCL5Cluster.AROUSAL.name.toLowerCase() to arousalScore
        )
    }

    private fun calculateBeckScores(answers: Map<String, Int>, questions: List<BeckQuestion>, scoringSystem: ScoringSystem): Map<String, Int> {
        val totalScore = answers.values.sum()
        // Группировка по категориям для Beck BDI-II
        val moodScore = calculateCategoryScore(answers, questions.map { it as DiagnosticQuestion }, QuestionCategory.MOOD)
        val cognitionScore = calculateCategoryScore(answers, questions.map { it as DiagnosticQuestion }, QuestionCategory.COGNITION)
        val physicalScore = calculateCategoryScore(answers, questions.map { it as DiagnosticQuestion }, QuestionCategory.PHYSICAL)
        val behaviorScore = calculateCategoryScore(answers, questions.map { it as DiagnosticQuestion }, QuestionCategory.SOCIAL) // Уточнить категории для BDI-II при добавлении всех вопросов

        return mapOf(
            "total" to totalScore,
            "mood" to moodScore,
            "cognition" to cognitionScore,
            "physical" to physicalScore,
            "behavior" to behaviorScore
            // TODO: Добавить баллы по остальным категориям/симптомам BDI-II
        )
    }

    private fun calculateCognitiveScores(answers: Map<String, Int>, questions: List<CognitiveQuestion>, scoringSystem: ScoringSystem): Map<String, Int> {
         val totalScore = answers.values.sum()
         val scoresByDistortionType = DistortionType.values().associate { distortionType ->
             distortionType.name.toLowerCase() to calculateDistortionScore(answers, questions, distortionType)
         }
        return scoresByDistortionType + ("total" to totalScore)
    }

     private fun calculateQOLScores(answers: Map<String, Int>, questions: List<QualityOfLifeQuestion>, scoringSystem: ScoringSystem): Map<String, Int> {
         // В WHOQOL-BREF вопросы 1 и 2 (общие) часто обрабатываются отдельно или включаются в общий балл.
         // Для простоты пока суммируем все баллы.
         val totalScore = answers.values.sum()

         val scoresByDomain = LifeDomain.values().associate { domain ->
             domain.name.toLowerCase() to calculateDomainScore(answers, questions, domain)
         }
         // Отдельно добавляем баллы за общие вопросы, если нужно их отслеживать
         val overallQOLScore = answers["qol_1"] ?: 0
         val healthSatisfactionScore = answers["qol_2"] ?: 0

        return scoresByDomain + mapOf("total" to totalScore, "overall_qol" to overallQOLScore, "health_satisfaction" to healthSatisfactionScore)
    }

    private fun calculateSleepScores(answers: Map<String, Int>, questions: List<SleepQuestion>, scoringSystem: ScoringSystem): Map<String, Int> {
        val totalScore = answers.values.sum()
        val scoresByDomain = SleepDomain.values().associate { domain ->
            domain.name.toLowerCase() to calculateDomainScore(answers, questions, domain)
        }
        return scoresByDomain + mapOf("total" to totalScore)
    }

    private fun calculateStressScores(answers: Map<String, Int>, questions: List<StressQuestion>, scoringSystem: ScoringSystem): Map<String, Int> {
        val totalScore = answers.values.sum()
        val scoresByDomain = StressDomain.values().associate { domain ->
            domain.name.toLowerCase() to calculateDomainScore(answers, questions, domain)
        }
        return scoresByDomain + mapOf("total" to totalScore)
    }

    private fun calculateClusterScore(answers: Map<String, Int>, questions: List<PCL5Question>, cluster: PCL5Cluster): Int {
        return questions
            .filter { it.cluster == cluster }
            .sumOf { answers[it.id] ?: 0 }
    }

    private fun calculateCategoryScore(answers: Map<String, Int>, questions: List<DiagnosticQuestion>, category: QuestionCategory): Int {
        return questions
            .filter { it.category == category }
            .sumOf { answers[it.id] ?: 0 }
    }

     private fun calculateDistortionScore(answers: Map<String, Int>, questions: List<CognitiveQuestion>, distortionType: DistortionType): Int {
         return questions
             .filter { it.distortionType == distortionType }
             .sumOf { answers[it.id] ?: 0 }
     }

     private fun calculateDomainScore(answers: Map<String, Int>, questions: List<SleepQuestion>, domain: SleepDomain): Int {
         return questions
             .filter { it.domain == domain }
             .sumOf { answers[it.id] ?: 0 }
     }

    private fun interpretScores(testId: String, scores: Map<String, Int>): String {
        val totalScore = scores["total"] ?: 0
        val test = getAvailableTests().find { it.id == testId }

        if (test == null) return "Неизвестный тест."

        // Интерпретация на основе общего балла и порогов
        val interpretationText = test.scoringSystem.thresholds.find {
            totalScore >= it.min && totalScore <= it.max
        }?.let { threshold ->
             when (testId) {
                "pcl5" -> "По результатам шкалы ПТСР (PCL-5), у вас ${threshold.level} уровень симптомов ПТСР. ${test.scoringSystem.interpretation["total"]}"
                "beck" -> "По результатам шкалы депрессии Бека (BDI-II), у вас ${threshold.level} уровень депрессивных симптомов. ${test.scoringSystem.interpretation["total"]}"
                 "cognitive" -> "По результатам теста когнитивных искажений, у вас ${threshold.level} уровень выраженности искажений. ${test.scoringSystem.interpretation["total"]}"
                 "qol" -> "По результатам оценки качества жизни, у вас ${threshold.level} качество жизни. ${test.scoringSystem.interpretation["total"]}"
                "sleep" -> "По результатам оценки качества сна, у вас ${threshold.level} качество сна. ${test.scoringSystem.interpretation["total"]}"
                "stress" -> "По результатам оценки уровня стресса, у вас ${threshold.level} уровень воспринимаемого стресса. ${test.scoringSystem.interpretation["total"]}"
                else -> "Ваш общий балл: $totalScore. Уровень: ${threshold.level}"
            }
        } ?: "Не удалось определить уровень по баллам." // Fallback если балл вне порогов

        // Добавление интерпретации по подшкалам/кластерам/доменам
         val detailedInterpretation = scores.filterKeys { it != "total" } // Исключаем общий балл из детальной интерпретации
            .mapNotNull { (key, score) ->
                // Используем ключ для поиска соответствующего описания из interpretation map
                val meaning = test.scoringSystem.interpretation[key]
                if (meaning != null) {
                    "$meaning Балл: $score"
                } else {
                    null // Пропускаем ключи без описания интерпретации
                }
            }
             .joinToString("\n")

        return if (detailedInterpretation.isNotEmpty()) {
            "$interpretationText\n\nДетали:\n$detailedInterpretation"
        } else {
            interpretationText
        }
    }

    private fun generateRecommendations(testId: String, scores: Map<String, Int>): List<String> {
        val totalScore = scores["total"] ?: 0
        val test = getAvailableTests().find { it.id == testId }

        if (test == null) return emptyList()

        // Рекомендации на основе общего балла и порогов
        val thresholdRecommendations = test.scoringSystem.thresholds.find {
            totalScore >= it.min && totalScore <= it.max
        }?.recommendations ?: emptyList()

        val specificRecommendations = mutableListOf<String>()

        // Добавление рекомендаций на основе баллов по подшкалам/кластерам (пример)
        when (testId) {
            "pcl5" -> {
                val intrusionScore = scores[PCL5Cluster.INTRUSION.name.toLowerCase()] ?: 0
                val avoidanceScore = scores[PCL5Cluster.AVOIDANCE.name.toLowerCase()] ?: 0
                // ... и так далее для других кластеров
                if (intrusionScore > 8) {
                     specificRecommendations.add("Обратите внимание на техники работы с навязчивыми воспоминаниями (например, экспозиция, переработка).")
                }
                if (avoidanceScore > 4) {
                    specificRecommendations.add("Постепенно и безопасно сталкивайтесь с ситуациями, которые вы избегаете.")
                }
                 // TODO: Добавить более детальные рекомендации по кластерам PCL-5
            }
            "beck" -> {
                 val moodScore = scores["mood"] ?: 0
                 val suicidalThoughtsScore = scores["suicidal_thoughts"] ?: 0
                 // ... и так далее для других категорий
                 if (suicidalThoughtsScore >= 2) {
                      specificRecommendations.add("Немедленно обратитесь за экстренной психологической помощью. Вы не одиноки.")
                 }
                 if (moodScore > 4) {
                     specificRecommendations.add("Попробуйте практики активации поведения и поиск поддерживающих занятий.")
                 }
                 // TODO: Добавить более детальные рекомендации по категориям Beck BDI-II
            }
             "cognitive" -> {
                 val allOrNothingScore = scores[DistortionType.ALL_OR_NOTHING.name.toLowerCase()] ?: 0
                 if (allOrNothingScore > 1) {
                     specificRecommendations.add("Осознанно ищите промежуточные варианты между 'все' и 'ничего'.")
                 }
                 // TODO: Добавить рекомендации по другим типам когнитивных искажений
             }
             "qol" -> {
                  val physicalScore = scores[LifeDomain.PHYSICAL.name.toLowerCase()] ?: 0
                 if (physicalScore < 8) { // Пример порога
                     specificRecommendations.add("Подумайте о постепенном увеличении физической активности или консультации с врачом.")
                 }
                 // TODO: Добавить рекомендации по другим доменам качества жизни
             }
             "sleep" -> {
                 val sleepQualityScore = scores[SleepDomain.SLEEP_QUALITY.name.toLowerCase()] ?: 0
                 if (sleepQualityScore < 5) {
                     specificRecommendations.add("Подумайте о улучшении гигиены сна или консультации со специалистом по сну.")
                 }
             }
             "stress" -> {
                 val physicalStressScore = scores[StressDomain.PHYSICAL.name.toLowerCase()] ?: 0
                 if (physicalStressScore > 13) {
                     specificRecommendations.add("Попробуйте использовать эффективные стратегии преодоления физического стресса.")
                 }
             }
        }

        return (thresholdRecommendations + specificRecommendations).distinct()
    }

    private fun determineRiskLevel(testId: String, scores: Map<String, Int>): RiskLevel {
        val totalScore = scores["total"] ?: 0
        val test = getAvailableTests().find { it.id == testId }

        if (test == null) return RiskLevel.LOW // Или SEVERE для неизвестного теста?

        // Определение уровня риска на основе общего балла и порогов
        val baseRiskLevel = test.scoringSystem.thresholds.find {
            totalScore >= it.min && totalScore <= it.max
        }?.level?.let { level ->
            when (level) {
                "Минимальный", "Низкий уровень", "Высокое качество жизни" -> RiskLevel.LOW
                "Легкая", "Средний уровень", "Среднее качество жизни" -> RiskLevel.MODERATE
                "Умеренный", "Высокий" -> RiskLevel.HIGH
                 "Тяжелая", "Низкое качество жизни" -> RiskLevel.SEVERE
                else -> RiskLevel.LOW // По умолчанию
            }
        } ?: RiskLevel.SEVERE // Считаем высокий риск, если балл вне определенных порогов

        // Корректировка уровня риска на основе специфических показателей (пример)
         var finalRiskLevel = baseRiskLevel

         when (testId) {
             "beck" -> {
                 val suicidalThoughtsScore = scores["suicidal_thoughts"] ?: 0
                 if (suicidalThoughtsScore >= 2) { // Если есть суицидальные мысли
                      finalRiskLevel = RiskLevel.SEVERE // Повышаем риск до максимального
                 }
                 // TODO: Добавить другие специфические факторы риска из Beck BDI-II
             }
             "pcl5" -> {
                  // В PCL-5 высокий балл по любому кластеру может указывать на необходимость внимания
                  val anyHighCluster = scores.filterKeys { it != "total" }.any { (key, score) ->
                       // Примерный порог для кластера, нужно уточнить
                      score > 10 // Пример: если балл по кластеру выше 10
                  }
                 if (baseRiskLevel != RiskLevel.SEVERE && anyHighCluster) {
                     // Если общий риск неSEVERE, но есть высокий балл по кластеру, возможно, стоит повысить до HIGH
                     if (finalRiskLevel < RiskLevel.HIGH) finalRiskLevel = RiskLevel.HIGH
                 }
                  // TODO: Добавить другие специфические факторы риска из PCL-5
             }
              // TODO: Добавить логику определения риска для других тестов
         }

        return finalRiskLevel
    }

    private fun calculateTrends(testId: String, userId: String, currentScores: Map<String, Int>): List<DiagnosticTrend> {
        // TODO: Реализовать получение предыдущих результатов из базы данных
        // Здесь будет логика сравнения с предыдущими результатами
        return emptyList()
    }

    private fun generateComparativeAnalysis(testId: String, scores: Map<String, Int>): ComparativeAnalysis? {
        // TODO: Реализовать сравнение с нормативными данными
        return null
    }

    private fun generateDetailedBreakdown(testId: String, scores: Map<String, Int>): Map<String, DetailedScore> {
        return scores.mapValues { (_, score) ->
            DetailedScore(
                rawScore = score,
                normalizedScore = normalizeScore(score),
                percentile = calculatePercentile(score),
                interpretation = generateScoreInterpretation(score),
                subScores = emptyMap() // TODO: Добавить подсчет подшкал
            )
        }
    }

    private fun normalizeScore(score: Int): Double {
        // TODO: Реализовать нормализацию баллов
        return score.toDouble()
    }

    private fun calculatePercentile(score: Int): Int {
        // TODO: Реализовать расчет процентиля
        return 50
    }

    private fun generateScoreInterpretation(score: Int): String {
        // TODO: Реализовать генерацию интерпретации
        return "Интерпретация балла: $score"
    }
} 