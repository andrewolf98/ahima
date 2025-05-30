package com.anima.data.db

import android.content.Context // Импортируем Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.anima.data.*
import com.anima.data.diagnostic.DiagnosticResult
import com.anima.data.diagnostic.RiskLevel
import com.google.gson.Gson // Потребуется зависимость Gson
import com.google.gson.reflect.TypeToken // Потребуется зависимость Gson
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset // Используем ZoneOffset для примера конвертации Instant <-> LocalDateTime

// Определяем классы конвертеров типов
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

    // TypeConverter для списка строк (для tasksCompleted в DailyProgressEntity и recommendations in DiagnosticEventEntity)
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

    // TypeConverter для DiagnosticResult (если решим хранить как JSON)
    @TypeConverter
    fun fromDiagnosticResult(result: DiagnosticResult?): String? {
        return gson.toJson(result)
    }

     @TypeConverter
    fun toDiagnosticResult(resultJson: String?): DiagnosticResult? {
        if (resultJson == null) return null
        val type = object : TypeToken<DiagnosticResult>() {}.type
        return gson.fromJson(resultJson, type)
    }
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

    // Статический метод для получения экземпляра базы данных (синглтон)
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
} 