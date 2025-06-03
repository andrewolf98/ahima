package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.relax.BreathingExercise
import com.example.ahima.audio.RelaxSoundGenerator
import com.example.ahima.model.UserProfile
import com.example.ahima.storage.LocalStorage

@Composable
fun RelaxScreen() {
    val profile = LocalStorage.getProfile()
    val sound = RelaxSoundGenerator.generateForProfile(profile)
    val breathing = BreathingExercise.getRecommendedPattern(profile.gad7Score)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Релаксация", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Рекомендованное дыхание: ${breathing.name} (${breathing.inhale}/${breathing.hold}/${breathing.exhale})")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Рекомендованный звук: $sound")
    }
}
