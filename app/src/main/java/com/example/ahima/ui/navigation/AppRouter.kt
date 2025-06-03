package com.example.ahima.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ahima.ui.*

object AppRouter {

    @Composable
    fun SetupNavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "neuro") {
            composable("neuro") { NeuroLevelScreen() }
            composable("daily") { DailyTaskScreen() }
            composable("relax") { RelaxScreen() }
            composable("support") { SupportScreen() }
            composable("chat") { AiChatScreen() }
            composable("export") { ExportScreen(profile = com.example.ahima.storage.LocalStorage.getProfile()) }
        }
    }
}
