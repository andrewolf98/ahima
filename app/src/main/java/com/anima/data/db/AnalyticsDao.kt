package com.anima.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anima.data.diagnostic.DiagnosticResult
import java.time.LocalDate

@Dao
interface DailyProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyProgress(progress: DailyProgressEntity)

    @Query("SELECT * FROM daily_progress WHERE userId = :userId ORDER BY date DESC")
    suspend fun getDailyProgress(userId: String): List<DailyProgressEntity>

    @Query("SELECT * FROM daily_progress WHERE userId = :userId ORDER BY date DESC LIMIT :limit")
    suspend fun getLatestDailyProgress(userId: String, limit: Int): List<DailyProgressEntity>

    @Query("SELECT * FROM daily_progress WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getDailyProgressByDateRange(userId: String, startDate: LocalDate, endDate: LocalDate): List<DailyProgressEntity>
}

@Dao
interface DiagnosticEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDiagnosticEvent(event: DiagnosticEventEntity)

    @Query("SELECT * FROM diagnostic_events WHERE userId = :userId ORDER BY dateTime DESC")
    suspend fun getDiagnosticEvents(userId: String): List<DiagnosticEventEntity>

     @Query("SELECT * FROM diagnostic_events WHERE userId = :userId AND testId = :testId ORDER BY dateTime DESC")
    suspend fun getDiagnosticEventsForTest(userId: String, testId: String): List<DiagnosticEventEntity>

    // TODO: Возможно, добавить запросы для получения DiagnosticResult из событий, если они хранятся денормализованно
    // (сейчас DiagnosticResult - отдельная модель данных, не сущность Room)
}

@Dao
interface GameEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGameEvent(event: GameEventEntity)

    @Query("SELECT * FROM game_events WHERE userId = :userId ORDER BY dateTime DESC")
    suspend fun getGameEvents(userId: String): List<GameEventEntity>

    @Query("SELECT * FROM game_events WHERE userId = :userId AND gameId = :gameId ORDER BY dateTime DESC")
    suspend fun getGameEventsForGame(userId: String, gameId: String): List<GameEventEntity>

     @Query("SELECT * FROM game_events WHERE userId = :userId AND gameId = :gameId AND levelId = :levelId ORDER BY dateTime DESC")
    suspend fun getGameEventsForLevel(userId: String, gameId: String, levelId: String): List<GameEventEntity>

    // TODO: Добавить запросы для агрегации игровых данных (например, средний балл, время прохождения уровня)
}

@Dao
interface RelaxationEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelaxationEvent(event: RelaxationEventEntity)

    @Query("SELECT * FROM relaxation_events WHERE userId = :userId ORDER BY dateTime DESC")
    suspend fun getRelaxationEvents(userId: String): List<RelaxationEventEntity>

     @Query("SELECT * FROM relaxation_events WHERE userId = :userId AND sessionId = :sessionId ORDER BY dateTime DESC")
    suspend fun getRelaxationEventsForSession(userId: String, sessionId: String): List<RelaxationEventEntity>

     // TODO: Добавить запросы для агрегации данных релаксации (например, средняя оценка)
}

@Dao
interface GeneratedContentConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedContentConfig(config: GeneratedContentConfigEntity)

    @Query("SELECT * FROM generated_content_configs WHERE userId = :userId ORDER BY dateTime DESC") // Предполагается, что в сущности будет поле dateTime
    suspend fun getGeneratedContentConfigs(userId: String): List<GeneratedContentConfigEntity>

    @Query("SELECT * FROM generated_content_configs WHERE userId = :userId ORDER BY dateTime DESC LIMIT 1") // Пример: получить последнюю сгенерированную конфигурацию
    suspend fun getLastGeneratedContentConfig(userId: String): GeneratedContentConfigEntity?
} 