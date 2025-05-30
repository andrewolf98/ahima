package com.anima.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anima.data.diagnostic.ТипИгры
import com.anima.ui.screens.*

sealed class GameScreen(val route: String) {
    object GameList : GameScreen("game_list")
    object EmotionSorter : GameScreen("emotion_sorter")
    object StroopTest : GameScreen("stroop_test")
    object NumberMemory : GameScreen("number_memory")
    object SpotTheDifference : GameScreen("spot_difference")
    object WordAssociation : GameScreen("word_association")
    object ReactionTime : GameScreen("reaction_time")
    object CardSorting : GameScreen("card_sorting")
    object IntonationRecognition : GameScreen("intonation_recognition")
    object SpatialTest : GameScreen("spatial_test")
    object FlankerTask : GameScreen("flanker_task")
    object Results : GameScreen("results")
}

@Composable
fun GameNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = GameScreen.GameList.route
    ) {
        composable(GameScreen.GameList.route) {
            GameListScreen(
                onGameSelected = { gameType ->
                    when (gameType) {
                        ТипИгры.ЭМОЦИОНАЛЬНЫЙ_СОРТЕР -> navController.navigate(GameScreen.EmotionSorter.route)
                        ТипИгры.СТРУП_ТЕСТ -> navController.navigate(GameScreen.StroopTest.route)
                        ТипИгры.ПАМЯТЬ_ЧИСЕЛ -> navController.navigate(GameScreen.NumberMemory.route)
                        ТипИгры.ПОИСК_ОТЛИЧИЙ -> navController.navigate(GameScreen.SpotTheDifference.route)
                        ТипИгры.АССОЦИАЦИИ_СЛОВ -> navController.navigate(GameScreen.WordAssociation.route)
                        ТипИгры.ВРЕМЯ_РЕАКЦИИ -> navController.navigate(GameScreen.ReactionTime.route)
                        ТипИгры.СОРТИРОВКА_КАРТОЧЕК -> navController.navigate(GameScreen.CardSorting.route)
                        ТипИгры.РАСПОЗНАВАНИЕ_ИНТОНАЦИЙ -> navController.navigate(GameScreen.IntonationRecognition.route)
                        ТипИгры.ПРОСТРАНСТВЕННЫЙ_ТЕСТ -> navController.navigate(GameScreen.SpatialTest.route)
                        ТипИгры.FLANKER_TASK -> navController.navigate(GameScreen.FlankerTask.route)
                    }
                }
            )
        }
        
        composable(GameScreen.EmotionSorter.route) {
            EmotionSorterScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.StroopTest.route) {
            StroopTestScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.NumberMemory.route) {
            NumberMemoryScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.SpotTheDifference.route) {
            SpotTheDifferenceScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.WordAssociation.route) {
            WordAssociationScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.ReactionTime.route) {
            ReactionTimeScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.CardSorting.route) {
            CardSortingScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.IntonationRecognition.route) {
            IntonationRecognitionScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.SpatialTest.route) {
            SpatialTestScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.FlankerTask.route) {
            FlankerTaskScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.navigate(GameScreen.Results.route) }
            )
        }
        
        composable(GameScreen.Results.route) {
            ResultsScreen(
                onBack = { navController.popBackStack() },
                onNewGame = { navController.navigate(GameScreen.GameList.route) }
            )
        }
    }
} 