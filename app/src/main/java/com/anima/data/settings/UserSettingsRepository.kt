package com.anima.data.settings

data class UserSettings(
    val language: String,
    val countryCode: String,
    // Добавьте другие настройки пользователя здесь
)

// Моковый репозиторий настроек пользователя
class UserSettingsRepository {
    // TODO: Реализовать загрузку и сохранение настроек пользователя
    fun getUserSettings(): UserSettings {
        // Возвращаем моковые настройки для примера
        return UserSettings(
            language = "ru",
            countryCode = "RU" // Моковый код страны
        )
    }

    // TODO: Добавить функцию для обновления настроек пользователя
} 