package com.anima.data.monitoring

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MentalStateMonitor {
    // Настроение
    suspend fun saveMoodEntry(entry: MoodEntry)
    fun getMoodEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MoodEntry>>
    
    // Скорость реакции
    suspend fun saveReactionTimeEntry(entry: ReactionTimeEntry)
    fun getReactionTimeEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<ReactionTimeEntry>>
    
    // Эмоциональный дневник
    suspend fun saveDrawingEntry(entry: DrawingEntry)
    fun getDrawingEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<DrawingEntry>>
    
    // Память чисел
    suspend fun saveMemoryTestEntry(entry: MemoryTestEntry)
    fun getMemoryTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MemoryTestEntry>>
    
    // Тест Струпа
    suspend fun saveStroopTestEntry(entry: StroopTestEntry)
    fun getStroopTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<StroopTestEntry>>
    
    // Баланс шарика
    suspend fun saveBalanceTestEntry(entry: BalanceTestEntry)
    fun getBalanceTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<BalanceTestEntry>>
    
    // Ассоциации слов
    suspend fun saveWordAssociationEntry(entry: WordAssociationEntry)
    fun getWordAssociationEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WordAssociationEntry>>
    
    // Ритм-тест
    suspend fun saveRhythmTestEntry(entry: RhythmTestEntry)
    fun getRhythmTestEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<RhythmTestEntry>>
    
    // Сортировка эмоций
    suspend fun saveEmotionSortingEntry(entry: EmotionSortingEntry)
    fun getEmotionSortingEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EmotionSortingEntry>>
    
    // Сон
    suspend fun saveSleepEntry(entry: SleepEntry)
    fun getSleepEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<SleepEntry>>
    
    // Общая сводка
    suspend fun saveMentalStateSummary(summary: MentalStateSummary)
    fun getMentalStateSummaries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MentalStateSummary>>
    
    // Анализ данных
    suspend fun analyzeMentalState(startDate: LocalDateTime, endDate: LocalDateTime): MentalStateSummary
    suspend fun generateReport(startDate: LocalDateTime, endDate: LocalDateTime): String // PDF report
    suspend fun checkAlerts(): List<String> // Проверка на отклонения от нормы
    
    // Игра на память
    suspend fun saveMemoryGameEntry(entry: MemoryGameEntry)
    fun getMemoryGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MemoryGameEntry>>
    
    // Игра на внимание
    suspend fun saveAttentionGameEntry(entry: AttentionGameEntry)
    fun getAttentionGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<AttentionGameEntry>>
    
    // Игра на эмоции
    suspend fun saveEmotionGameEntry(entry: EmotionGameEntry)
    fun getEmotionGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EmotionGameEntry>>
    
    // Когнитивная игра
    suspend fun saveCognitiveGameEntry(entry: CognitiveGameEntry)
    fun getCognitiveGameEntries(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<CognitiveGameEntry>>
} 