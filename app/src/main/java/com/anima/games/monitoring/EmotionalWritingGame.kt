package com.anima.games.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anima.data.monitoring.Emotion
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.data.monitoring.WritingEntry
import java.time.LocalDateTime

data class WritingState(
    val currentEmotion: Emotion? = null,
    val text: String = "",
    val isSaved: Boolean = false,
    val feedback: String = ""
)

class EmotionalWritingGame(
    private val monitor: MentalStateMonitor
) {
    var state by mutableStateOf(WritingState())
        private set

    fun onEmotionSelected(emotion: Emotion) {
        state = state.copy(
            currentEmotion = emotion,
            isSaved = false
        )
    }

    fun updateText(text: String) {
        state = state.copy(text = text)
    }

    suspend fun saveEntry() {
        val emotion = state.currentEmotion ?: return
        
        val entry = WritingEntry(
            date = LocalDateTime.now(),
            text = state.text,
            emotion = emotion
        )
        
        monitor.saveWritingEntry(entry)
        state = state.copy(
            isSaved = true,
            feedback = "Запись сохранена!"
        )
    }

    fun getEmotionText(emotion: Emotion): String = when (emotion) {
        Emotion.HAPPY -> "Радость"
        Emotion.CALM -> "Спокойствие"
        Emotion.SAD -> "Грусть"
        Emotion.ANGRY -> "Гнев"
        Emotion.ANXIOUS -> "Тревога"
        Emotion.EXCITED -> "Возбуждение"
    }

    fun getEmotionPrompt(emotion: Emotion): String = when (emotion) {
        Emotion.HAPPY -> "Опишите, что вызывает у вас радость и почему это важно для вас"
        Emotion.CALM -> "Расскажите о моментах, когда вы чувствуете себя спокойно и умиротворенно"
        Emotion.SAD -> "Поделитесь своими мыслями о том, что вызывает у вас грусть"
        Emotion.ANGRY -> "Опишите ситуации, которые вызывают у вас гнев, и как вы с этим справляетесь"
        Emotion.ANXIOUS -> "Расскажите о своих тревогах и страхах, и как вы с ними боретесь"
        Emotion.EXCITED -> "Поделитесь тем, что вас волнует и вызывает возбуждение"
    }
} 