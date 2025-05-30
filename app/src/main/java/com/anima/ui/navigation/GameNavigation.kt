package com.anima.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anima.data.monitoring.MentalStateMonitor
import com.anima.games.monitoring.*
import com.anima.ui.games.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MemoryGame : Screen("memory_game")
    object AttentionGame : Screen("attention_game")
    object EmotionGame : Screen("emotion_game")
    object CognitiveGame : Screen("cognitive_game")
}

@Composable
fun GameNavigation(
    monitor: MentalStateMonitor,
    onGameComplete: () -> Unit
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onMemoryGameClick = { navController.navigate(Screen.MemoryGame.route) },
                onAttentionGameClick = { navController.navigate(Screen.AttentionGame.route) },
                onEmotionGameClick = { navController.navigate(Screen.EmotionGame.route) },
                onCognitiveGameClick = { navController.navigate(Screen.CognitiveGame.route) }
            )
        }
        
        composable(Screen.MemoryGame.route) {
            val game = remember { MemoryGame(monitor) }
            MemoryGameScreen(
                game = game,
                onGameComplete = {
                    onGameComplete()
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.AttentionGame.route) {
            val game = remember { AttentionGame(monitor) }
            AttentionGameScreen(
                game = game,
                onGameComplete = {
                    onGameComplete()
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.EmotionGame.route) {
            val game = remember { EmotionGame(monitor) }
            EmotionGameScreen(
                game = game,
                onGameComplete = {
                    onGameComplete()
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.CognitiveGame.route) {
            val game = remember { CognitiveGame(monitor) }
            CognitiveGameScreen(
                game = game,
                onGameComplete = {
                    onGameComplete()
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    onMemoryGameClick: () -> Unit,
    onAttentionGameClick: () -> Unit,
    onEmotionGameClick: () -> Unit,
    onCognitiveGameClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Anima",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Button(
            onClick = onMemoryGameClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Игра на память")
        }
        
        Button(
            onClick = onAttentionGameClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Игра на внимание")
        }
        
        Button(
            onClick = onEmotionGameClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Дневник эмоций")
        }
        
        Button(
            onClick = onCognitiveGameClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Когнитивные игры")
        }
    }
} 