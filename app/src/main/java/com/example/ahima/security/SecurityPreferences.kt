package com.example.ahima.security

import android.content.Context
import android.content.SharedPreferences

object SecurityPreferences {

    private const val PREF_NAME = "ahima_security"
    private const val KEY_PIN = "user_pin"

    fun savePin(context: Context, pin: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PIN, pin).apply()
    }

    fun getPin(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PIN, null)
    }

    fun clearPin(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_PIN).apply()
    }
}
