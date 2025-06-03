package com.example.ahima.audio

import android.util.Log
import java.util.*
import com.example.ahima.model.UserProfile

object RelaxMixRecommender {

    fun recommend(profile: UserProfile): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour in 22..23 || hour in 0..6 -> {
                Log.d("RelaxMix", "Рекомендовано: ночной сон")
                "sleep_mix.mp3"
            }
            profile.gad7Score >= 12 -> {
                Log.d("RelaxMix", "Рекомендовано: антистресс микс")
                "focus_relief_mix.mp3"
            }
            else -> {
                Log.d("RelaxMix", "Рекомендовано: дневная концентрация")
                "day_focus_mix.mp3"
            }
        }
    }
}
