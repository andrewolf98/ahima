package com.example.ahima.system

import android.util.Log

object AppLogger {
    private const val TAG = "AHIMA"

    fun info(message: String) {
        Log.i(TAG, message)
    }

    fun debug(message: String) {
        Log.d(TAG, message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}
