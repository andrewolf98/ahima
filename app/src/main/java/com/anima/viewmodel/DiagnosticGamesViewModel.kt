package com.anima.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.diagnostic.*
import com.anima.games.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiagnosticGamesViewModel : ViewModel() {
    private val _currentGame = MutableStateFlow<DiagnosticGameEngine?>(null)
    val currentGame: StateFlow<DiagnosticGameEngine?> = _currentGame

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Результаты всех игр
    private val gameResults = mutableListOf<GameResult>()

    fun startGame(type: ТипИгры, config: Any) {
        viewModelScope.launch {
            try {
                val game = DiagnosticGameFactory.createGame(type, config)
                game.start()
                _currentGame.value = game
                _score.value = 0
                _total.value = game.getTotal()
                _isFinished.value = false
            } catch (e: Exception) {
                _error.value = "Ошибка при запуске игры: ${e.message}"
            }
        }
    }

    fun submitAnswer(answer: String) {
        viewModelScope.launch {
            try {
                val game = _currentGame.value ?: return@launch
                val isCorrect = game.submitAnswer("", answer)
                
                if (isCorrect) {
                    _score.value = game.getScore()
                }
                
                if (game.isFinished()) {
                    _isFinished.value = true
                    saveGameResult(game)
                }
            } catch (e: Exception) {
                _error.value = "Ошибка при проверке ответа: ${e.message}"
            }
        }
    }

    private fun saveGameResult(game: DiagnosticGameEngine) {
        val result = GameResult(
            score = game.getScore(),
            total = game.getTotal(),
            timeSpent = System.currentTimeMillis(),
            mistakes = 0, // TODO: добавить подсчет ошибок
            gameType = when (game) {
                is EmotionSorterGame -> ТипИгры.ЭМОЦИОНАЛЬНЫЙ_СОРТЕР
                is StroopTestGame -> ТипИгры.СТРУП_ТЕСТ
                is NumberMemoryGame -> ТипИгры.ПАМЯТЬ_ЧИСЕЛ
                is SpotTheDifferenceGame -> ТипИгры.ПОИСК_ОТЛИЧИЙ
                is WordAssociationGame -> ТипИгры.АССОЦИАЦИИ_СЛОВ
                is ReactionTimeGame -> ТипИгры.ВРЕМЯ_РЕАКЦИИ
                is CardSortingGame -> ТипИгры.СОРТИРОВКА_КАРТОЧЕК
                is IntonationRecognitionGame -> ТипИгры.РАСПОЗНАВАНИЕ_ИНТОНАЦИЙ
                is SpatialTestGame -> ТипИгры.ПРОСТРАНСТВЕННЫЙ_ТЕСТ
                is FlankerTaskGame -> ТипИгры.FLANKER_TASK
                else -> throw IllegalArgumentException("Неизвестный тип игры")
            }
        )
        gameResults.add(result)
    }

    fun getGameResults(): List<GameResult> = gameResults.toList()

    fun resetGame() {
        _currentGame.value = null
        _score.value = 0
        _total.value = 0
        _isFinished.value = false
        _error.value = null
    }
} 