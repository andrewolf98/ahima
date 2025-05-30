package com.anima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.diagnostic.ТипИгры
import com.anima.data.repository.GameResultRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GameStatistics(
    val totalGames: Int = 0,
    val averageScore: Double = 0.0,
    val bestScore: Int = 0,
    val averageTime: Long = 0,
    val totalMistakes: Int = 0
)

class StatisticsViewModel(
    private val repository: GameResultRepository
) : ViewModel() {

    private val _selectedGameType = MutableStateFlow<ТипИгры?>(null)
    val selectedGameType: StateFlow<ТипИгры?> = _selectedGameType

    private val _statistics = MutableStateFlow(GameStatistics())
    val statistics: StateFlow<GameStatistics> = _statistics

    init {
        viewModelScope.launch {
            _selectedGameType
                .flatMapLatest { gameType ->
                    if (gameType == null) {
                        repository.getAllResults()
                    } else {
                        repository.getResultsByGameType(gameType)
                    }
                }
                .collect { results ->
                    updateStatistics(results)
                }
        }
    }

    fun selectGameType(gameType: ТипИгры?) {
        _selectedGameType.value = gameType
    }

    private fun updateStatistics(results: List<GameResultEntity>) {
        if (results.isEmpty()) {
            _statistics.value = GameStatistics()
            return
        }

        val totalGames = results.size
        val averageScore = results.map { it.score.toDouble() / it.total }.average()
        val bestScore = results.maxOf { (it.score.toDouble() / it.total * 100).toInt() }
        val averageTime = results.map { it.timeSpent }.average().toLong()
        val totalMistakes = results.sumOf { it.mistakes }

        _statistics.value = GameStatistics(
            totalGames = totalGames,
            averageScore = averageScore,
            bestScore = bestScore,
            averageTime = averageTime,
            totalMistakes = totalMistakes
        )
    }

    fun clearStatistics() {
        viewModelScope.launch {
            repository.deleteAllResults()
        }
    }
} 