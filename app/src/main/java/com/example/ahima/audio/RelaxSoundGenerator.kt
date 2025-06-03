package com.example.ahima.audio

import com.example.ahima.model.UserProfile

object RelaxSoundGenerator {
    fun generateForProfile(profile: UserProfile): String {
        val score = profile.phq9Score + profile.gad7Score
        return when {
            score >= 25 -> "sounds/storm.ogg"
            score >= 15 -> "sounds/rain.ogg"
            else -> "sounds/forest.ogg"
        }
    }
}
