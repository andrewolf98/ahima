package com.anima.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val country: String,
    val depressionScore: Int,
    val anxietyScore: Int,
    val lastTestDate: LocalDateTime?,
    val diagnosis: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "daily_progress")
data class DailyProgressEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val date: LocalDateTime,
    val completedTasks: Int,
    val mood: String,
    val anxietyLevel: Int,
    val productivity: Int,
    val meditationMinutes: Int,
    val sleepHours: Float,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val testId: String,
    val score: Int,
    val severity: String,
    val recommendations: String,
    val date: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val type: String,
    val date: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUser(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}

@Dao
interface DailyProgressDao {
    @Query("SELECT * FROM daily_progress WHERE userId = :userId ORDER BY date DESC")
    fun getDailyProgress(userId: String): Flow<List<DailyProgressEntity>>

    @Query("SELECT * FROM daily_progress WHERE userId = :userId AND date = :date")
    suspend fun getDailyProgressByDate(userId: String, date: LocalDateTime): DailyProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyProgress(progress: DailyProgressEntity)

    @Update
    suspend fun updateDailyProgress(progress: DailyProgressEntity)

    @Delete
    suspend fun deleteDailyProgress(progress: DailyProgressEntity)
}

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_results WHERE userId = :userId ORDER BY date DESC")
    fun getTestResults(userId: String): Flow<List<TestResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultEntity)

    @Update
    suspend fun updateTestResult(result: TestResultEntity)

    @Delete
    suspend fun deleteTestResult(result: TestResultEntity)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements WHERE userId = :userId ORDER BY date DESC")
    fun getAchievements(userId: String): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: AchievementEntity)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Delete
    suspend fun deleteAchievement(achievement: AchievementEntity)
}

@Database(
    entities = [
        UserEntity::class,
        DailyProgressEntity::class,
        TestResultEntity::class,
        AchievementEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AnimaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dailyProgressDao(): DailyProgressDao
    abstract fun testResultDao(): TestResultDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: AnimaDatabase? = null

        fun getInstance(context: android.content.Context): AnimaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimaDatabase::class.java,
                    "anima_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @androidx.room.TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @androidx.room.TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
} 