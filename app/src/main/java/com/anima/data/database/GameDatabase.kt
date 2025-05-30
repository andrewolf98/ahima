package com.anima.data.database

import androidx.room.*
import com.anima.data.diagnostic.ТипИгры
import java.util.*

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameType: ТипИгры,
    val score: Int,
    val total: Int,
    val timeSpent: Long,
    val mistakes: Int,
    val timestamp: Date
)

@Dao
interface GameResultDao {
    @Query("SELECT * FROM game_results ORDER BY timestamp DESC")
    suspend fun getAllResults(): List<GameResultEntity>

    @Query("SELECT * FROM game_results WHERE gameType = :gameType ORDER BY timestamp DESC")
    suspend fun getResultsByGameType(gameType: ТипИгры): List<GameResultEntity>

    @Insert
    suspend fun insertResult(result: GameResultEntity)

    @Delete
    suspend fun deleteResult(result: GameResultEntity)

    @Query("DELETE FROM game_results")
    suspend fun deleteAllResults()
}

@Database(entities = [GameResultEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromGameType(value: ТипИгры): String {
        return value.name
    }

    @TypeConverter
    fun toGameType(value: String): ТипИгры {
        return ТипИгры.valueOf(value)
    }
} 