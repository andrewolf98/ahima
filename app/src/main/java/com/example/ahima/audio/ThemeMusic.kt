package com.example.ahima.audio

import com.example.ahima.model.UserProfile

object ThemeMusic {
    fun getTrackFor(profile: UserProfile): String {
        val score = profile.phq9Score + profile.gad7Score
        return when {
            score >= 25 -> "dark_theme.mp3"
            score >= 15 -> "calm_focus.mp3"
            else -> "light_joy.mp3"
        }
    }
}
