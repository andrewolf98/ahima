package com.example.ahima.system

import android.content.Context

object UserPreferences {

    private const val PREF_NAME = "ahima_user_prefs"

    fun saveString(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, default: String = ""): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, default) ?: default
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, default: Boolean = false): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, default)
    }
}
