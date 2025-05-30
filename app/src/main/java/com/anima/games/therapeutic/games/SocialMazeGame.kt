package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class SocialSituation(
    val id: Int,
    val description: String,
    val options: List<ResponseOption>,
    val position: Pair<Float, Float>,
    val isCompleted: Boolean = false
)

data class ResponseOption(
    val id: Int,
    val text: String,
    val isCorrect: Boolean,
    val feedback: String
)

data class MazeState(
    val situations: List<SocialSituation> = emptyList(),
    val currentSituation: SocialSituation? = null,
    val score: Int = 0,
    val feedback: String = ""
)

class SocialMazeGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var mistakes = 0

    private val _mazeState = MutableStateFlow(MazeState())
    val mazeState: StateFlow<MazeState> = _mazeState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    private val socialSituations = listOf(
        SocialSituation(
            id = 1,
            description = "Ваш друг выглядит расстроенным. Что вы сделаете?",
            options = listOf(
                ResponseOption(
                    id = 1,
                    text = "Спросить, что случилось",
                    isCorrect = true,
                    feedback = "Правильно! Проявление интереса и эмпатии помогает укрепить дружбу."
                ),
                ResponseOption(
                    id = 2,
                    text = "Игнорировать, это не мои проблемы",
                    isCorrect = false,
                    feedback = "Игнорирование чувств друга может привести к отдалению."
                ),
                ResponseOption(
                    id = 3,
                    text = "Начать рассказывать о своих проблемах",
                    isCorrect = false,
                    feedback = "Сейчас важно выслушать друга, а не переключать внимание на себя."
                )
            ),
            position = Pair(0.2f, 0.2f)
        ),
        SocialSituation(
            id = 2,
            description = "В группе обсуждают тему, в которой вы не разбираетесь. Ваши действия?",
            options = listOf(
                ResponseOption(
                    id = 1,
                    text = "Честно признаться в незнании и попросить объяснить",
                    isCorrect = true,
                    feedback = "Отлично! Проявление искренности и желания учиться - это нормально."
                ),
                ResponseOption(
                    id = 2,
                    text = "Притвориться, что знаете тему",
                    isCorrect = false,
                    feedback = "Притворство может привести к неловким ситуациям."
                ),
                ResponseOption(
                    id = 3,
                    text = "Молча уйти",
                    isCorrect = false,
                    feedback = "Уход от общения не решает проблему и может вызвать недопонимание."
                )
            ),
            position = Pair(0.5f, 0.3f)
        ),
        SocialSituation(
            id = 3,
            description = "Кто-то сделал вам комплимент. Как отреагировать?",
            options = listOf(
                ResponseOption(
                    id = 1,
                    text = "Искренне поблагодарить",
                    isCorrect = true,
                    feedback = "Правильно! Принятие комплимента с благодарностью - признак уверенности."
                ),
                ResponseOption(
                    id = 2,
                    text = "Начать оправдываться",
                    isCorrect = false,
                    feedback = "Оправдания могут показать неуверенность в себе."
                ),
                ResponseOption(
                    id = 3,
                    text = "Сделать комплимент в ответ",
                    isCorrect = false,
                    feedback = "Автоматический ответный комплимент может выглядеть неискренне."
                )
            ),
            position = Pair(0.8f, 0.4f)
        ),
        SocialSituation(
            id = 4,
            description = "Вы опоздали на встречу. Что скажете?",
            options = listOf(
                ResponseOption(
                    id = 1,
                    text = "Извиниться и кратко объяснить причину",
                    isCorrect = true,
                    feedback = "Хорошо! Краткое объяснение и извинение показывают уважение к времени других."
                ),
                ResponseOption(
                    id = 2,
                    text = "Придумать оправдание",
                    isCorrect = false,
                    feedback = "Ложь может подорвать доверие в отношениях."
                ),
                ResponseOption(
                    id = 3,
                    text = "Не обращать внимания на опоздание",
                    isCorrect = false,
                    feedback = "Игнорирование опоздания может показать неуважение к другим."
                )
            ),
            position = Pair(0.3f, 0.6f)
        ),
        SocialSituation(
            id = 5,
            description = "Кто-то делится с вами своими достижениями. Как отреагировать?",
            options = listOf(
                ResponseOption(
                    id = 1,
                    text = "Искренне порадоваться и поддержать",
                    isCorrect = true,
                    feedback = "Отлично! Искренняя радость за других укрепляет отношения."
                ),
                ResponseOption(
                    id = 2,
                    text = "Рассказать о своих достижениях",
                    isCorrect = false,
                    feedback = "Сейчас важно разделить радость собеседника, а не переключать внимание на себя."
                ),
                ResponseOption(
                    id = 3,
                    text = "Отмахнуться",
                    isCorrect = false,
                    feedback = "Пренебрежение достижениями других может обидеть."
                )
            ),
            position = Pair(0.7f, 0.7f)
        )
    )

    override fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        generateNewMaze()
        _remainingTime.value = config.duration
    }

    override fun pause() {
        isRunning = false
    }

    override fun resume() {
        isRunning = true
    }

    override fun stop() {
        isRunning = false
    }

    override fun getScore(): Int = score

    override fun getProgress(): Float {
        val timeSpent = System.currentTimeMillis() - startTime
        return (timeSpent.toFloat() / config.duration).coerceIn(0f, 1f)
    }

    override fun getTimeSpent(): Long = System.currentTimeMillis() - startTime

    override fun isFinished(): Boolean = getProgress() >= 1f

    override fun getTherapeuticFeedback(): String {
        val accuracy = if (mistakes == 0) "отличная" else "хорошая"
        return "Вы успешно справились с социальными ситуациями с $accuracy точностью. " +
               "Продолжайте развивать навыки социального взаимодействия для улучшения отношений с окружающими."
    }

    fun onSituationSelected(situationId: Int) {
        if (!isRunning) return

        val currentState = _mazeState.value
        val situation = currentState.situations.find { it.id == situationId } ?: return

        _mazeState.value = currentState.copy(
            currentSituation = situation
        )
    }

    fun onResponseSelected(optionId: Int) {
        if (!isRunning) return

        val currentState = _mazeState.value
        val situation = currentState.currentSituation ?: return
        val option = situation.options.find { it.id == optionId } ?: return

        if (option.isCorrect) {
            score++
            _mazeState.value = currentState.copy(
                situations = currentState.situations.map {
                    if (it.id == situation.id) it.copy(isCompleted = true) else it
                },
                currentSituation = null,
                feedback = option.feedback
            )
        } else {
            mistakes++
            _mazeState.value = currentState.copy(
                feedback = option.feedback
            )
        }
    }

    private fun generateNewMaze() {
        val shuffledSituations = socialSituations.shuffled()
        _mazeState.value = MazeState(
            situations = shuffledSituations
        )
    }
} 