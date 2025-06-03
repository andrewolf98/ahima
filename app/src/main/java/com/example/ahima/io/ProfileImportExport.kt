package com.example.ahima.io

import android.content.Context
import com.example.ahima.model.UserProfile

object ProfileImportExport {

    fun exportProfile(context: Context, profile: UserProfile) {
        // Заглушка: сохранить профиль в файл
    }

    fun importProfile(context: Context): UserProfile {
        // Заглушка: вернуть тестовый профиль
        return UserProfile(phq9Score = 5, gad7Score = 3)
    }
}
