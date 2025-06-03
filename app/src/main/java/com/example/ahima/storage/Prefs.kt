package com.example.ahima.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.ahima.model.UserProfile

object Prefs {
    private const val PREFS_NAME = "ahima_prefs"

    fun saveProfile(context: Context, profile: UserProfile) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("phq9", profile.phq9Score)
            putInt("gad7", profile.gad7Score)
            apply()
        }
    }

    fun loadProfile(context: Context): UserProfile {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return UserProfile(
            phq9Score = prefs.getInt("phq9", 0),
            gad7Score = prefs.getInt("gad7", 0)
        )
    }
}
