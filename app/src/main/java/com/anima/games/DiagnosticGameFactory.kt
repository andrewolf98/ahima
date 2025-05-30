package com.anima.games

import com.anima.data.diagnostic.*

// Результат игры
data class GameResult(
    val score: Int,
    val total: Int,
    val timeSpent: Long,
    val mistakes: Int,
    val gameType: ТипИгры,
    val timestamp: Long = System.currentTimeMillis()
)

// Фабрика для создания игр
object DiagnosticGameFactory {
    fun createGame(type: ТипИгры, config: Any): DiagnosticGameEngine {
        return when (type) {
            ТипИгры.ЭМОЦИОНАЛЬНЫЙ_СОРТЕР -> EmotionSorterGame(config as КонфигЭмоциональногоСортера)
            ТипИгры.СТРУП_ТЕСТ -> StroopTestGame(config as КонфигСтрупТеста)
            ТипИгры.ПАМЯТЬ_ЧИСЕЛ -> NumberMemoryGame(config as КонфигПамятиЧисел)
            ТипИгры.ПОИСК_ОТЛИЧИЙ -> SpotTheDifferenceGame(config as КонфигПоискаОтличий)
            ТипИгры.АССОЦИАЦИИ_СЛОВ -> WordAssociationGame(config as КонфигАссоциацийСлов)
            ТипИгры.ВРЕМЯ_РЕАКЦИИ -> ReactionTimeGame(config as КонфигВремениРеакции)
            ТипИгры.СОРТИРОВКА_КАРТОЧЕК -> CardSortingGame(config as КонфигСортировкиКарточек)
            ТипИгры.РАСПОЗНАВАНИЕ_ИНТОНАЦИЙ -> IntonationRecognitionGame(config as КонфигРаспознаванияИнтонаций)
            ТипИгры.ПРОСТРАНСТВЕННЫЙ_ТЕСТ -> SpatialTestGame(config as КонфигПространственногоТеста)
            ТипИгры.FLANKER_TASK -> FlankerTaskGame(config as КонфигFlankerTask)
        }
    }
} 