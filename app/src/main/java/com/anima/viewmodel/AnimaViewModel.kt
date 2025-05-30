package com.anima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AnimaViewModel : ViewModel() {
    private val _userState = MutableStateFlow<UserState?>(null)
    val userState: StateFlow<UserState?> = _userState.asStateFlow()

    private val _dailyTasks = MutableStateFlow<List<DailyTask>>(emptyList())
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserState()
        generateDailyTasks()
    }

    private fun loadUserState() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Загрузить данные из локального хранилища или с сервера
                _userState.value = createMockUserState()
            } catch (e: Exception) {
                // TODO: Обработка ошибок
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun generateDailyTasks() {
        viewModelScope.launch {
            // TODO: Генерация заданий на основе психологического профиля
            _dailyTasks.value = createMockDailyTasks()
        }
    }

    fun updateUserProfile(name: String, age: Int, country: String) {
        viewModelScope.launch {
            _userState.value?.let { currentState ->
                _userState.value = currentState.copy(
                    name = name,
                    age = age,
                    country = country
                )
                // TODO: Сохранить изменения
            }
        }
    }

    fun updateSettings(settings: UserSettings) {
        viewModelScope.launch {
            _userState.value?.let { currentState ->
                _userState.value = currentState.copy(settings = settings)
                // TODO: Сохранить изменения
            }
        }
    }

    fun completeTask(taskId: String, rating: Int? = null) {
        viewModelScope.launch {
            _dailyTasks.value = _dailyTasks.value.map { task ->
                if (task.id == taskId) task.copy(isCompleted = true, rating = rating)
                else task
            }
            // TODO: Обновить прогресс пользователя
        }
    }

    fun updateMood(mood: Mood) {
        viewModelScope.launch {
            _userState.value?.let { currentState ->
                val newProgress = currentState.dailyProgress.copy(mood = mood)
                _userState.value = currentState.copy(dailyProgress = newProgress)
                // TODO: Сохранить изменения
            }
        }
    }

    // Временные моковые данные для тестирования
    private fun createMockUserState(): UserState {
        return UserState(
            id = "1",
            name = "Иван",
            age = 25,
            country = "Россия",
            psychologicalProfile = PsychologicalProfile(
                phq9Score = 5,
                gad7Score = 4,
                lastTestDate = LocalDateTime.now(),
                diagnosis = null,
                riskFactors = listOf(
                    RiskFactor(
                        type = RiskType.ANXIETY,
                        level = RiskLevel.MEDIUM,
                        description = "Повышенная тревожность"
                    )
                )
            ),
            dailyProgress = DailyProgress(
                date = LocalDateTime.now(),
                completedTasks = emptyList(),
                mood = Mood.NEUTRAL,
                anxiety = 5,
                productivity = 7,
                meditationMinutes = 0,
                sleepHours = 0f
            ),
            settings = UserSettings(
                notificationsEnabled = true,
                darkTheme = false,
                language = "Русский",
                syncEnabled = true,
                analyticsEnabled = true,
                soundVolume = 0.7f,
                autoPlay = false,
                backgroundPlay = true
            )
        )
    }

    private fun createMockDailyTasks(): List<DailyTask> {
        return listOf(
            DailyTask(
                id = "1",
                title = "Утренняя медитация",
                description = "10 минут медитации для начала дня",
                type = TaskType.MEDITATION,
                duration = 10,
                isCompleted = false,
                rating = null
            ),
            DailyTask(
                id = "2",
                title = "Дыхательное упражнение",
                description = "Техника 4-7-8 для снятия стресса",
                type = TaskType.BREATHING,
                duration = 5,
                isCompleted = false,
                rating = null
            ),
            DailyTask(
                id = "3",
                title = "Когнитивное упражнение",
                description = "Запись позитивных мыслей",
                type = TaskType.COGNITIVE,
                duration = 15,
                isCompleted = false,
                rating = null
            )
        )
    }
}

data class DailyTask(
    val id: String,
    val title: String,
    val description: String,
    val type: TaskType,
    val duration: Int,
    val isCompleted: Boolean,
    val rating: Int?
) 