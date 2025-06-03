package com.example.ahima.system

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    fun applyTheme(context: Context, darkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
