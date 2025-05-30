package com.anima.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

interface AnimaRepository {
    suspend fun getUserState(userId: String): UserState?
    suspend fun saveUserState(userState: UserState)
    fun getDailyProgress(userId: String): Flow<List<DailyProgress>>
    suspend fun saveDailyProgress(userId: String, progress: DailyProgress)
    fun getTestResults(userId: String): Flow<List<TestResult>>
    suspend fun saveTestResult(userId: String, result: TestResult)
    fun getAchievements(userId: String): Flow<List<Achievement>>
    suspend fun saveAchievement(userId: String, achievement: Achievement)
    suspend fun getDailyTasks(userId: String, date: LocalDateTime): List<DailyTask>
    suspend fun completeTask(taskId: String, rating: Int?)
    suspend fun updateProgress(progress: DailyProgress)
    suspend fun getMeditations(): List<Meditation>
    suspend fun getSounds(): List<Sound>
    suspend fun getBreathingExercises(): List<BreathingExercise>
    suspend fun getMixModes(): List<MixMode>
}

class AnimaRepositoryImpl(private val context: Context) : AnimaRepository {
    private val database = AnimaDatabase.getInstance(context)
    private val userDao = database.userDao()
    private val dailyProgressDao = database.dailyProgressDao()
    private val testResultDao = database.testResultDao()
    private val achievementDao = database.achievementDao()

    override suspend fun getUserState(userId: String): UserState? {
        return userDao.getUser(userId)?.let { UserMapper.toDomain(it) }
    }

    override suspend fun saveUserState(userState: UserState) {
        userDao.insertUser(UserMapper.toEntity(userState))
    }

    override fun getDailyProgress(userId: String): Flow<List<DailyProgress>> {
        return dailyProgressDao.getDailyProgress(userId)
            .map { entities -> entities.map { DailyProgressMapper.toDomain(it) } }
    }

    override suspend fun saveDailyProgress(userId: String, progress: DailyProgress) {
        dailyProgressDao.insertDailyProgress(DailyProgressMapper.toEntity(progress, userId))
    }

    override fun getTestResults(userId: String): Flow<List<TestResult>> {
        return testResultDao.getTestResults(userId)
            .map { entities -> entities.map { TestResultMapper.toDomain(it) } }
    }

    override suspend fun saveTestResult(userId: String, result: TestResult) {
        testResultDao.insertTestResult(TestResultMapper.toEntity(result, userId))
    }

    override fun getAchievements(userId: String): Flow<List<Achievement>> {
        return achievementDao.getAchievements(userId)
            .map { entities -> entities.map { AchievementMapper.toDomain(it) } }
    }

    override suspend fun saveAchievement(userId: String, achievement: Achievement) {
        achievementDao.insertAchievement(AchievementMapper.toEntity(achievement, userId))
    }

    override suspend fun getDailyTasks(userId: String, date: LocalDateTime): List<DailyTask> {
        // TODO: Реализовать получение задач из базы данных
        return createMockDailyTasks()
    }

    override suspend fun completeTask(taskId: String, rating: Int?) {
        // TODO: Реализовать обновление задачи в базе данных
    }

    override suspend fun updateProgress(progress: DailyProgress) {
        // TODO: Реализовать обновление прогресса в базе данных
    }

    override suspend fun getMeditations(): List<Meditation> {
        // TODO: Реализовать получение медитаций из базы данных
        return createMockMeditations()
    }

    override suspend fun getSounds(): List<Sound> {
        // TODO: Реализовать получение звуков из базы данных
        return createMockSounds()
    }

    override suspend fun getBreathingExercises(): List<BreathingExercise> {
        // TODO: Реализовать получение дыхательных упражнений из базы данных
        return createMockBreathingExercises()
    }

    override suspend fun getMixModes(): List<MixMode> {
        // TODO: Реализовать получение миксов из базы данных
        return createMockMixModes()
    }

    private fun createMockDailyTasks(): List<DailyTask> {
        return listOf(
            DailyTask(
                id = "1",
                title = "Утренняя медитация",
                description = "Начните день с 10-минутной медитации",
                type = TaskType.MEDITATION,
                duration = 10,
                isCompleted = false,
                rating = 0
            ),
            DailyTask(
                id = "2",
                title = "Дыхательные упражнения",
                description = "Выполните 5 минут дыхательных упражнений",
                type = TaskType.BREATHING,
                duration = 5,
                isCompleted = false,
                rating = 0
            ),
            DailyTask(
                id = "3",
                title = "Прогулка на свежем воздухе",
                description = "Совершите 30-минутную прогулку",
                type = TaskType.ACTIVITY,
                duration = 30,
                isCompleted = false,
                rating = 0
            )
        )
    }

    private fun createMockMeditations(): List<Meditation> {
        return listOf(
            Meditation(
                id = "1",
                title = "Утренняя медитация",
                description = "Начните день с осознанности",
                duration = 10,
                audioUrl = "meditation_morning.mp3",
                category = "Утро"
            ),
            Meditation(
                id = "2",
                title = "Снятие стресса",
                description = "Избавьтесь от напряжения",
                duration = 15,
                audioUrl = "meditation_stress.mp3",
                category = "Стресс"
            )
        )
    }

    private fun createMockSounds(): List<Sound> {
        return listOf(
            Sound(
                id = "1",
                title = "Дождь",
                description = "Звуки дождя",
                duration = 60,
                audioUrl = "sound_rain.mp3",
                category = "Природа"
            ),
            Sound(
                id = "2",
                title = "Океан",
                description = "Звуки океана",
                duration = 60,
                audioUrl = "sound_ocean.mp3",
                category = "Природа"
            )
        )
    }

    private fun createMockBreathingExercises(): List<BreathingExercise> {
        return listOf(
            BreathingExercise(
                id = "1",
                title = "4-7-8",
                description = "Техника дыхания для снятия стресса",
                inhaleDuration = 4,
                holdDuration = 7,
                exhaleDuration = 8,
                cycles = 4
            ),
            BreathingExercise(
                id = "2",
                title = "Квадратное дыхание",
                description = "Техника для концентрации",
                inhaleDuration = 4,
                holdDuration = 4,
                exhaleDuration = 4,
                cycles = 5
            )
        )
    }

    private fun createMockMixModes(): List<MixMode> {
        return listOf(
            MixMode(
                id = "1",
                title = "Сон",
                description = "Комбинация для улучшения сна",
                meditationId = "2",
                soundId = "1",
                duration = 30
            ),
            MixMode(
                id = "2",
                title = "Концентрация",
                description = "Комбинация для улучшения концентрации",
                meditationId = "1",
                soundId = "2",
                duration = 20
            )
        )
    }
}

data class Meditation(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int,
    val audioUrl: String,
    val category: String
)

data class Sound(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int,
    val audioUrl: String,
    val category: String
)

data class BreathingExercise(
    val id: String,
    val title: String,
    val description: String,
    val inhaleDuration: Int,
    val holdDuration: Int,
    val exhaleDuration: Int,
    val cycles: Int
)

data class MixMode(
    val id: String,
    val title: String,
    val description: String,
    val meditationId: String,
    val soundId: String,
    val duration: Int
) 