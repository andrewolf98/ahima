package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.Mood
import com.anima.data.monitoring.MoodEntry
import java.time.LocalDateTime

data class KaleidoscopeState(
    val selectedMood: Mood? = null,
    val selectedColor: Color? = null,
    val intensity: Int = 5,
    val isSaved: Boolean = false,
    val feedback: String = ""
)

class MoodKaleidoscopeGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(KaleidoscopeState())
        private set

    fun onMoodSelected(mood: Mood) {
        state = state.copy(
            selectedMood = mood,
            isSaved = false
        )
    }

    fun onColorSelected(color: Color) {
        state = state.copy(
            selectedColor = color,
            isSaved = false
        )
    }

    fun onIntensityChanged(intensity: Int) {
        state = state.copy(
            intensity = intensity,
            isSaved = false
        )
    }

    suspend fun saveMood() {
        val mood = state.selectedMood ?: return
        val color = state.selectedColor ?: return

        val entry = MoodEntry(
            date = LocalDateTime.now(),
            mood = mood,
            color = color.toArgb().toString(16),
            intensity = state.intensity
        )

        monitor.saveMoodEntry(entry)
        state = state.copy(
            isSaved = true,
            feedback = "Настроение сохранено!"
        )
    }

    fun getMoodColor(mood: Mood): Color = when (mood) {
        Mood.HAPPY -> Color(0xFFFFEB3B) // Желтый
        Mood.CALM -> Color(0xFF81C784) // Зеленый
        Mood.NEUTRAL -> Color(0xFF90CAF9) // Синий
        Mood.ANXIOUS -> Color(0xFFFFB74D) // Оранжевый
        Mood.SAD -> Color(0xFF64B5F6) // Голубой
        Mood.ANGRY -> Color(0xFFE57373) // Красный
    }

    fun getMoodText(mood: Mood): String = when (mood) {
        Mood.HAPPY -> "Радость"
        Mood.CALM -> "Спокойствие"
        Mood.NEUTRAL -> "Нейтральное"
        Mood.ANXIOUS -> "Тревога"
        Mood.SAD -> "Грусть"
        Mood.ANGRY -> "Гнев"
    }
} 