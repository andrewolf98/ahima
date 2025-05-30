package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.anima.data.monitoring.DrawingEntry
import com.anima.data.monitoring.Emotion
import com.anima.data.monitoring.MentalStateMonitor
import java.time.LocalDateTime

data class DrawingState(
    val currentEmotion: Emotion? = null,
    val drawingPath: List<DrawingPath> = emptyList(),
    val currentPath: DrawingPath? = null,
    val isSaved: Boolean = false,
    val feedback: String = ""
)

data class DrawingPath(
    val points: List<Point>,
    val color: Color,
    val strokeWidth: Float
)

data class Point(
    val x: Float,
    val y: Float
)

class EmotionalDrawingGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(DrawingState())
        private set

    fun onEmotionSelected(emotion: Emotion) {
        state = state.copy(
            currentEmotion = emotion,
            isSaved = false
        )
    }

    fun startNewPath(color: Color, strokeWidth: Float) {
        state = state.copy(
            currentPath = DrawingPath(
                points = emptyList(),
                color = color,
                strokeWidth = strokeWidth
            )
        )
    }

    fun addPointToPath(point: Point) {
        val currentPath = state.currentPath ?: return
        state = state.copy(
            currentPath = currentPath.copy(
                points = currentPath.points + point
            )
        )
    }

    fun finishPath() {
        val currentPath = state.currentPath ?: return
        state = state.copy(
            drawingPath = state.drawingPath + currentPath,
            currentPath = null
        )
    }

    fun clearDrawing() {
        state = state.copy(
            drawingPath = emptyList(),
            currentPath = null,
            isSaved = false
        )
    }

    suspend fun saveDrawing(description: String) {
        val emotion = state.currentEmotion ?: return
        
        // В реальном приложении здесь будет сохранение изображения
        val imageUrl = "drawing_${System.currentTimeMillis()}.png"
        
        val entry = DrawingEntry(
            date = LocalDateTime.now(),
            imageUrl = imageUrl,
            emotion = emotion,
            description = description
        )
        
        monitor.saveDrawingEntry(entry)
        state = state.copy(
            isSaved = true,
            feedback = "Рисунок сохранен!"
        )
    }

    fun getEmotionColor(emotion: Emotion): Color = when (emotion) {
        Emotion.HAPPY -> Color(0xFFFFEB3B) // Желтый
        Emotion.CALM -> Color(0xFF81C784) // Зеленый
        Emotion.SAD -> Color(0xFF64B5F6) // Голубой
        Emotion.ANGRY -> Color(0xFFE57373) // Красный
        Emotion.ANXIOUS -> Color(0xFFFFB74D) // Оранжевый
        Emotion.EXCITED -> Color(0xFFBA68C8) // Фиолетовый
    }

    fun getEmotionText(emotion: Emotion): String = when (emotion) {
        Emotion.HAPPY -> "Радость"
        Emotion.CALM -> "Спокойствие"
        Emotion.SAD -> "Грусть"
        Emotion.ANGRY -> "Гнев"
        Emotion.ANXIOUS -> "Тревога"
        Emotion.EXCITED -> "Возбуждение"
    }
} 