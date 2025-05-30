package com.anima.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.anima.data.*
import com.anima.data.diagnostic.DiagnosticResult
import com.anima.data.diagnostic.RiskLevel // Импортируем RiskLevel
import java.time.LocalDate
import java.time.LocalDateTime

// Сущность для ежедневного прогресса
@Entity(tableName = "daily_progress")
data class DailyProgressEntity(
    @PrimaryKey val date: LocalDate, // Дата как первичный ключ
    val mood: Int,
    val anxiety: Int,
    val productivity: Int,
    val meditationMinutes: Int,
    // TODO: Добавить поле для списка выполненных задач (потребуется TypeConverter)
    val tasksCompletedJson: String // Пример хранения списка как JSON строки
)

// Сущность для событий диагностики
@Entity(tableName = "diagnostic_events")
data class DiagnosticEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val testId: String,
    val dateTime: LocalDateTime, // Используем LocalDateTime
    val eventType: String, // Храним Enum как строку
    val questionId: String?,
    val answer: Int?,
    val score: Int? = null, // Балл за тест или подшкалу, если событие связано с ответом (или общий балл для TEST_COMPLETED)
    val resultId: String? = null, // ID результата, если тест завершен
    // Новые поля для результатов теста (для eventType = TEST_COMPLETED)
    val totalScore: Int? = null,
    val riskLevel: String? = null, // Храним RiskLevel как строку
    val interpretation: String? = null,
    val recommendationsJson: String? = null // Храним список рекомендаций как JSON строку
)

// Сущность для игровых событий
@Entity(tableName = "game_events")
data class GameEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val gameId: String,
    val dateTime: LocalDateTime,
    val eventType: String, // Храним Enum как строку
    val levelId: String?,
    val score: Int?,
    val timeSpent: Long?,
    val success: Boolean? // Room поддерживает Boolean
)

// Сущность для событий релаксации
@Entity(tableName = "relaxation_events")
data class RelaxationEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val sessionId: String,
    val dateTime: LocalDateTime,
    val eventType: String, // Храним Enum как строку
    val duration: Long?,
    val rating: Int?,
    val feedback: String? // Храним String
)

// Сущность для конфигурации сгенерированного контента
@Entity(tableName = "generated_content_configs")
data class GeneratedContentConfigEntity(
    @PrimaryKey val configId: String, // Возможно, уникальный ID конфигурации
    val userId: String,
    val contentType: String, // Храним Enum как строку
    // TODO: Хранение GameConfig, LevelConfig, RelaxationConfig (потребуются TypeConverter'ы или отдельные связанные таблицы)
    val configJson: String // Пример хранения конфигурации как JSON строки
)

// TODO: Добавить TypeConverter'ы для LocalDateTime, LocalDate, списков, мап и перечислений (Enum)

/* Пример TypeConverter для LocalDateTime:
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        // Конвертация Long (Unix timestamp в миллисекундах) в LocalDateTime
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC) } // Пример для UTC
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        // Конвертация LocalDateTime в Long (Unix timestamp в миллисекундах)
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli() // Пример для UTC
    }

    @TypeConverter
    fun fromLocalDate(value: Long?): LocalDate? {
        // Конвертация Long (Unix timestamp дня) в LocalDate
         return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToLocalDate(date: LocalDate?): Long? {
        // Конвертация LocalDate в Long (Unix timestamp дня)
        return date?.toEpochDay()
    }
}
*/

/* Пример TypeConverter для Enum:
class EnumConverters {
    @TypeConverter
    fun fromDiagnosticEventType(value: DiagnosticEventType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toDiagnosticEventType(value: String?): DiagnosticEventType? {
        return value?.let { DiagnosticEventType.valueOf(it) }
    }

    @TypeConverter
    fun fromGameEventType(value: GameEventType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toGameEventType(value: String?): GameEventType? {
        return value?.let { GameEventType.valueOf(it) }
    }

     @TypeConverter
    fun fromRelaxationEventType(value: RelaxationEventType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toRelaxationEventType(value: String?): RelaxationEventType? {
        return value?.let { RelaxationEventType.valueOf(it) }
    }

    @TypeConverter
    fun fromGeneratedContentType(value: GeneratedContentType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toGeneratedContentType(value: String?): GeneratedContentType? {
        return value?.let { GeneratedContentType.valueOf(it) }
    }
    
    @TypeConverter
    fun fromRiskLevel(value: RiskLevel?): String? {
        return value?.name
    }

    @TypeConverter
    fun toRiskLevel(value: String?): RiskLevel? {
        return value?.let { RiskLevel.valueOf(it) }
    }
    // TODO: Добавить конвертеры для других Enum'ов, используемых в сущностях (если есть)
}

class JsonConverters {
    private val gson = Gson() // Потребуется зависимость Gson

    // TypeConverter для списка строк (для tasksCompleted в DailyProgressEntity и recommendations в DiagnosticEventEntity)
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(string: String?): List<String>? {
        if (string == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, type)
    }

    // TypeConverter для хранения сложных объектов (GameConfig, LevelConfig, RelaxationConfig) как JSON строки
    @TypeConverter
    fun fromGameConfigJson(config: GameConfig?): String? {
         return gson.toJson(config)
    }

    @TypeConverter
    fun toGameConfigJson(configJson: String?): GameConfig? {
        if (configJson == null) return null
        val type = object : TypeToken<GameConfig>() {}.type
        return gson.fromJson(configJson, type)
    }

     @TypeConverter
    fun fromLevelConfigJson(config: LevelConfig?): String? {
         return gson.toJson(config)
    }

    @TypeConverter
    fun toLevelConfigJson(configJson: String?): LevelConfig? {
        if (configJson == null) return null
        val type = object : TypeToken<LevelConfig>() {}.type
        return gson.fromJson(configJson, type)
    }

     @TypeConverter
    fun fromRelaxationConfigJson(config: RelaxationConfig?): String? {
         return gson.toJson(config)
    }

    @TypeConverter
    fun toRelaxationConfigJson(configJson: String?): RelaxationConfig? {
        if (configJson == null) return null
        val type = object : TypeToken<RelaxationConfig>() {}.type
        return gson.fromJson(configJson, type)
    }
    // TODO: Добавить конвертеры для других сложных объектов, если они будут храниться в JSON
}

@Database(
    entities = [
        DailyProgressEntity::class,
        DiagnosticEventEntity::class,
        GameEventEntity::class,
        RelaxationEventEntity::class,
        GeneratedContentConfigEntity::class
        // TODO: Добавить DiagnosticResultEntity, если решите хранить результаты диагностики в базе данных
    ],
    version = 1, // Начинаем с версии 1
    exportSchema = false // Обычно false для простых приложений, true для миграций
)
@TypeConverters(Converters::class, EnumConverters::class, JsonConverters::class) // Регистрируем конвертеры
abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyProgressDao(): DailyProgressDao
    abstract fun diagnosticEventDao(): DiagnosticEventDao
    abstract fun gameEventDao(): GameEventDao
    abstract fun relaxationEventDao(): RelaxationEventDao
    abstract fun generatedContentConfigDao(): GeneratedContentConfigDao

    // TODO: Добавить статический метод или Dagger/Hilt модуль для получения экземпляра базы данных
/* Пример статического метода получения экземпляра:
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "anima_database"
                )
                // .addMigrations(...) // Добавить миграции при изменении схемы
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
*/
} 