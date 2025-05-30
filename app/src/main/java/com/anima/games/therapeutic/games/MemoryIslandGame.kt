package com.anima.games.therapeutic.games

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class MemoryItem(
    val id: Int,
    val type: MemoryItemType,
    val position: Pair<Float, Float>,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)

enum class MemoryItemType {
    FLOWER,
    SHELL,
    STAR,
    CRYSTAL,
    LEAF,
    FEATHER
}

data class IslandState(
    val items: List<MemoryItem> = emptyList(),
    val selectedItems: List<MemoryItem> = emptyList(),
    val score: Int = 0,
    val level: Int = 1,
    val feedback: String = ""
)

class MemoryIslandGame(
    private val config: TherapeuticGameConfig
) : TherapeuticGameEngine {
    private var startTime: Long = 0
    private var isRunning = false
    private var score = 0
    private var level = 1
    private var mistakes = 0

    private val _islandState = MutableStateFlow(IslandState())
    val islandState: StateFlow<IslandState> = _islandState

    private val _remainingTime = MutableStateFlow(config.duration)
    val remainingTime: StateFlow<Long> = _remainingTime

    override fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        generateNewLevel()
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
        return "Вы успешно справились с запоминанием с $accuracy точностью. " +
               "Продолжайте тренировать память для улучшения концентрации внимания."
    }

    fun onItemSelected(itemId: Int) {
        if (!isRunning) return

        val currentState = _islandState.value
        val item = currentState.items.find { it.id == itemId } ?: return

        if (item.isRevealed || item.isMatched) return

        val updatedItems = currentState.items.map {
            if (it.id == itemId) it.copy(isRevealed = true) else it
        }

        val selectedItems = currentState.selectedItems + item

        if (selectedItems.size == 2) {
            val (first, second) = selectedItems
            if (first.type == second.type) {
                // Совпадение найдено
                score += 10 * level
                _islandState.value = currentState.copy(
                    items = updatedItems.map {
                        if (it.id == first.id || it.id == second.id) it.copy(isMatched = true) else it
                    },
                    selectedItems = emptyList(),
                    score = score,
                    feedback = "Отлично! +${10 * level} очков"
                )

                // Проверяем, все ли пары найдены
                if (updatedItems.all { it.isMatched }) {
                    level++
                    generateNewLevel()
                }
            } else {
                // Несовпадение
                mistakes++
                _islandState.value = currentState.copy(
                    items = updatedItems,
                    selectedItems = emptyList(),
                    feedback = "Попробуйте еще раз"
                )
            }
        } else {
            _islandState.value = currentState.copy(
                items = updatedItems,
                selectedItems = selectedItems
            )
        }
    }

    private fun generateNewLevel() {
        val itemTypes = MemoryItemType.values()
        val items = mutableListOf<MemoryItem>()
        val gridSize = 2 + level // Увеличиваем размер сетки с каждым уровнем

        // Создаем пары предметов
        repeat(gridSize * gridSize / 2) { index ->
            val type = itemTypes[index % itemTypes.size]
            val position1 = Pair(
                Random.nextFloat() * 0.8f + 0.1f,
                Random.nextFloat() * 0.8f + 0.1f
            )
            val position2 = Pair(
                Random.nextFloat() * 0.8f + 0.1f,
                Random.nextFloat() * 0.8f + 0.1f
            )

            items.add(
                MemoryItem(
                    id = index * 2,
                    type = type,
                    position = position1
                )
            )
            items.add(
                MemoryItem(
                    id = index * 2 + 1,
                    type = type,
                    position = position2
                )
            )
        }

        _islandState.value = IslandState(
            items = items.shuffled(),
            level = level
        )
    }
} 