package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.logic.AnalyticsEngine
import com.example.ahima.storage.LocalStorage

@Composable
fun AnalyticsScreen() {
    val profile = LocalStorage.getProfile()
    val insights = AnalyticsEngine.getInsights(profile)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Аналитика", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        for (insight in insights) {
            Text("${insight.label}: ${insight.value}")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
