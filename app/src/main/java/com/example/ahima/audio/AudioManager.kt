package com.example.ahima.audio

import android.content.Context
import com.example.ahima.model.UserProfile

object AudioManager {
    fun playRelaxationTrack(context: Context, profile: UserProfile) {
        val sound = RelaxSoundGenerator.generateForProfile(profile)
        SoundPlayer.play(context, sound)
    }

    fun playThemeTrack(context: Context, profile: UserProfile) {
        val track = ThemeMusic.getTrackFor(profile)
        SoundPlayer.play(context, track)
    }
}
