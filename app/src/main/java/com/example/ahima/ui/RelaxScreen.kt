package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Context

@Composable
fun RelaxScreen(context: Context) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Релакс-зона", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Медитации, дыхательные практики и нейрозвуки — в разработке.")
    }
}
