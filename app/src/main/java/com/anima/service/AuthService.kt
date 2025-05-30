package com.anima.service

import android.content.Context
import com.anima.data.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AuthService(private val context: Context) {
    private val _currentUser = MutableStateFlow<UserState?>(null)
    val currentUser: StateFlow<UserState?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    suspend fun signUp(name: String, age: Int, country: String): Result<UserState> {
        return try {
            _authState.value = AuthState.Authenticating
            // TODO: Реализовать регистрацию через Firebase Auth
            val user = UserState(
                id = UUID.randomUUID().toString(),
                name = name,
                age = age,
                country = country,
                psychologicalProfile = UserState.PsychologicalProfile(
                    depressionScore = 0,
                    anxietyScore = 0,
                    lastTestDate = null,
                    diagnosis = null,
                    riskFactors = emptyList()
                ),
                dailyProgress = UserState.DailyProgress(
                    completedTasks = 0,
                    mood = UserState.Mood.NEUTRAL,
                    anxietyLevel = 0,
                    productivity = 0,
                    meditationMinutes = 0,
                    sleepHours = 0f
                ),
                settings = UserState.UserSettings(
                    notificationsEnabled = true,
                    darkTheme = false,
                    language = "ru",
                    syncEnabled = true,
                    analyticsEnabled = true,
                    soundVolume = 1f,
                    autoPlay = true,
                    backgroundPlay = true
                )
            )
            _currentUser.value = user
            _authState.value = AuthState.Authenticated
            Result.success(user)
        } catch (e: Exception) {
            _authError.value = e.message
            _authState.value = AuthState.Error
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<UserState> {
        return try {
            _authState.value = AuthState.Authenticating
            // TODO: Реализовать вход через Firebase Auth
            // Временная заглушка для демонстрации
            val user = UserState(
                id = UUID.randomUUID().toString(),
                name = "Тестовый пользователь",
                age = 25,
                country = "Россия",
                psychologicalProfile = UserState.PsychologicalProfile(
                    depressionScore = 0,
                    anxietyScore = 0,
                    lastTestDate = null,
                    diagnosis = null,
                    riskFactors = emptyList()
                ),
                dailyProgress = UserState.DailyProgress(
                    completedTasks = 0,
                    mood = UserState.Mood.NEUTRAL,
                    anxietyLevel = 0,
                    productivity = 0,
                    meditationMinutes = 0,
                    sleepHours = 0f
                ),
                settings = UserState.UserSettings(
                    notificationsEnabled = true,
                    darkTheme = false,
                    language = "ru",
                    syncEnabled = true,
                    analyticsEnabled = true,
                    soundVolume = 1f,
                    autoPlay = true,
                    backgroundPlay = true
                )
            )
            _currentUser.value = user
            _authState.value = AuthState.Authenticated
            Result.success(user)
        } catch (e: Exception) {
            _authError.value = e.message
            _authState.value = AuthState.Error
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        try {
            // TODO: Реализовать выход через Firebase Auth
            _currentUser.value = null
            _authState.value = AuthState.NotAuthenticated
        } catch (e: Exception) {
            _authError.value = e.message
            _authState.value = AuthState.Error
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            _authState.value = AuthState.Authenticating
            // TODO: Реализовать сброс пароля через Firebase Auth
            _authState.value = AuthState.NotAuthenticated
            Result.success(Unit)
        } catch (e: Exception) {
            _authError.value = e.message
            _authState.value = AuthState.Error
            Result.failure(e)
        }
    }

    fun clearAuthError() {
        _authError.value = null
    }
}

sealed class AuthState {
    object NotAuthenticated : AuthState()
    object Authenticating : AuthState()
    object Authenticated : AuthState()
    object Error : AuthState()
}

class AuthPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var userId: String?
        get() = prefs.getString(KEY_USER_ID, null)
        set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "auth_preferences"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }
} 