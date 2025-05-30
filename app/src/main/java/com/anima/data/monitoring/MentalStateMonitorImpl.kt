package com.anima.data.monitoring

import com.anima.data.models.Emotion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class MentalStateMonitorImpl : MentalStateMonitor {
    private val moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    private val reactionTimeEntries = MutableStateFlow<List<ReactionTimeEntry>>(emptyList())
    private val drawingEntries = MutableStateFlow<List<DrawingEntry>>(emptyList())
    private val memoryTestEntries = MutableStateFlow<List<MemoryTestEntry>>(emptyList())
    private val stroopTestEntries = MutableStateFlow<List<StroopTestEntry>>(emptyList())
    private val balanceTestEntries = MutableStateFlow<List<BalanceTestEntry>>(emptyList())
    private val wordAssociationEntries = MutableStateFlow<List<WordAssociationEntry>>(emptyList())
    private val rhythmTestEntries = MutableStateFlow<List<RhythmTestEntry>>(emptyList())
    private val emotionSortingEntries = MutableStateFlow<List<EmotionSortingEntry>>(emptyList())
    private val sleepEntries = MutableStateFlow<List<SleepEntry>>(emptyList())
    private val mentalStateSummaries = MutableStateFlow<List<MentalStateSummary>>(emptyList())
    private val memoryGameEntries = MutableStateFlow<List<MemoryGameEntry>>(emptyList())
    private val attentionGameEntries = MutableStateFlow<List<AttentionGameEntry>>(emptyList())
    private val emotionGameEntries = MutableStateFlow<List<EmotionGameEntry>>(emptyList())
    private val cognitiveGameEntries = MutableStateFlow<List<CognitiveGameEntry>>(emptyList())

    override suspend fun saveMoodEntry(entry: MoodEntry) {
        moodEntries.value = moodEntries.value + entry
    }

    override fun getMoodEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MoodEntry>> {
        return moodEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveReactionTimeEntry(entry: ReactionTimeEntry) {
        reactionTimeEntries.value = reactionTimeEntries.value + entry
    }

    override fun getReactionTimeEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<ReactionTimeEntry>> {
        return reactionTimeEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveDrawingEntry(entry: DrawingEntry) {
        drawingEntries.value = drawingEntries.value + entry
    }

    override fun getDrawingEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<DrawingEntry>> {
        return drawingEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveMemoryTestEntry(entry: MemoryTestEntry) {
        memoryTestEntries.value = memoryTestEntries.value + entry
    }

    override fun getMemoryTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MemoryTestEntry>> {
        return memoryTestEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveStroopTestEntry(entry: StroopTestEntry) {
        stroopTestEntries.value = stroopTestEntries.value + entry
    }

    override fun getStroopTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<StroopTestEntry>> {
        return stroopTestEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveBalanceTestEntry(entry: BalanceTestEntry) {
        balanceTestEntries.value = balanceTestEntries.value + entry
    }

    override fun getBalanceTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<BalanceTestEntry>> {
        return balanceTestEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveWordAssociationEntry(entry: WordAssociationEntry) {
        wordAssociationEntries.value = wordAssociationEntries.value + entry
    }

    override fun getWordAssociationEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WordAssociationEntry>> {
        return wordAssociationEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveRhythmTestEntry(entry: RhythmTestEntry) {
        rhythmTestEntries.value = rhythmTestEntries.value + entry
    }

    override fun getRhythmTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<RhythmTestEntry>> {
        return rhythmTestEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveEmotionSortingEntry(entry: EmotionSortingEntry) {
        emotionSortingEntries.value = emotionSortingEntries.value + entry
    }

    override fun getEmotionSortingEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EmotionSortingEntry>> {
        return emotionSortingEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveSleepEntry(entry: SleepEntry) {
        sleepEntries.value = sleepEntries.value + entry
    }

    override fun getSleepEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<SleepEntry>> {
        return sleepEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveMentalStateSummary(summary: MentalStateSummary) {
        mentalStateSummaries.value = mentalStateSummaries.value + summary
    }

    override fun getMentalStateSummaries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MentalStateSummary>> {
        return mentalStateSummaries.map { summaries ->
            summaries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun analyzeMentalState(startDate: LocalDateTime, endDate: LocalDateTime): MentalStateSummary {
        // Простая реализация анализа
        val moodEntries = moodEntries.value.filter { it.date in startDate..endDate }
        val moodScore = if (moodEntries.isNotEmpty()) {
            moodEntries.map { it.intensity / 10f }.average().toFloat()
        } else 0f

        val emotionEntries = emotionGameEntries.value.filter { it.date in startDate..endDate }
        val anxietyScore = if (emotionEntries.isNotEmpty()) {
            emotionEntries
                .filter { it.emotion == Emotion.ANXIOUS }
                .map { it.intensity / 10f }
                .average()
                .toFloat()
        } else 0f

        val depressionScore = if (emotionEntries.isNotEmpty()) {
            emotionEntries
                .filter { it.emotion == Emotion.SAD }
                .map { it.intensity / 10f }
                .average()
                .toFloat()
        } else 0f

        val attentionEntries = attentionGameEntries.value.filter { it.date in startDate..endDate }
        val adhdScore = if (attentionEntries.isNotEmpty()) {
            attentionEntries
                .map { it.correctCount.toFloat() / it.targetCount }
                .average()
                .toFloat()
        } else 0f

        val sleepEntries = sleepEntries.value.filter { it.date in startDate..endDate }
        val sleepScore = if (sleepEntries.isNotEmpty()) {
            sleepEntries.map { it.quality / 10f }.average().toFloat()
        } else 0f

        val cognitiveEntries = cognitiveGameEntries.value.filter { it.date in startDate..endDate }
        val cognitiveScore = if (cognitiveEntries.isNotEmpty()) {
            cognitiveEntries.map { it.score / 100f }.average().toFloat()
        } else 0f

        return MentalStateSummary(
            date = LocalDateTime.now(),
            moodScore = moodScore,
            anxietyScore = anxietyScore,
            depressionScore = depressionScore,
            adhdScore = adhdScore,
            sleepScore = sleepScore,
            cognitiveScore = cognitiveScore,
            notes = "Анализ выполнен автоматически"
        )
    }

    override suspend fun generateReport(startDate: LocalDateTime, endDate: LocalDateTime): String {
        val summary = analyzeMentalState(startDate, endDate)
        return buildString {
            appendLine("Отчет о ментальном состоянии")
            appendLine("Период: $startDate - $endDate")
            appendLine()
            appendLine("Настроение: ${summary.moodScore * 100}%")
            appendLine("Тревожность: ${summary.anxietyScore * 100}%")
            appendLine("Депрессия: ${summary.depressionScore * 100}%")
            appendLine("СДВГ: ${summary.adhdScore * 100}%")
            appendLine("Сон: ${summary.sleepScore * 100}%")
            appendLine("Когнитивные способности: ${summary.cognitiveScore * 100}%")
            appendLine()
            appendLine("Примечания: ${summary.notes}")
        }
    }

    override suspend fun checkAlerts(): List<String> {
        val alerts = mutableListOf<String>()
        val recentSummary = analyzeMentalState(
            LocalDateTime.now().minusDays(7),
            LocalDateTime.now()
        )

        if (recentSummary.anxietyScore > 0.7f) {
            alerts.add("Повышенный уровень тревожности")
        }
        if (recentSummary.depressionScore > 0.7f) {
            alerts.add("Повышенный уровень депрессии")
        }
        if (recentSummary.sleepScore < 0.5f) {
            alerts.add("Низкое качество сна")
        }
        if (recentSummary.cognitiveScore < 0.5f) {
            alerts.add("Снижение когнитивных способностей")
        }

        return alerts
    }

    override suspend fun saveMemoryGameEntry(entry: MemoryGameEntry) {
        memoryGameEntries.value = memoryGameEntries.value + entry
    }

    override fun getMemoryGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MemoryGameEntry>> {
        return memoryGameEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveAttentionGameEntry(entry: AttentionGameEntry) {
        attentionGameEntries.value = attentionGameEntries.value + entry
    }

    override fun getAttentionGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<AttentionGameEntry>> {
        return attentionGameEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveEmotionGameEntry(entry: EmotionGameEntry) {
        emotionGameEntries.value = emotionGameEntries.value + entry
    }

    override fun getEmotionGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EmotionGameEntry>> {
        return emotionGameEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }

    override suspend fun saveCognitiveGameEntry(entry: CognitiveGameEntry) {
        cognitiveGameEntries.value = cognitiveGameEntries.value + entry
    }

    override fun getCognitiveGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<CognitiveGameEntry>> {
        return cognitiveGameEntries.map { entries ->
            entries.filter { it.date in startDate..endDate }
        }
    }
} 