package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.logic.RewardSystem
import com.example.ahima.storage.LocalStorage

@Composable
fun RewardScreen() {
    val profile = LocalStorage.getProfile()
    val points = RewardSystem.calculatePoints(profile.phq9Score, profile.gad7Score)
    val message = RewardSystem.getRewardMessage(points)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Награды", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ваш результат: $message")
    }
}
