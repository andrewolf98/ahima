package com.anima.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anima.screens.*

sealed class Screen(val route: String, val icon: Int, val label: String) {
    object Recovery : Screen("recovery", android.R.drawable.ic_menu_compass, "Восстановление")
    object Relax : Screen("relax", android.R.drawable.ic_menu_gallery, "Релакс-зона")
    object Profile : Screen("profile", android.R.drawable.ic_menu_myplaces, "Профиль")
    object Analytics : Screen("analytics", android.R.drawable.ic_menu_report_image, "Аналитика")
    object Support : Screen("support", android.R.drawable.ic_menu_help, "Поддержка")
    object Settings : Screen("settings", android.R.drawable.ic_menu_preferences, "Настройки")
    object Diagnostic : Screen("diagnostic", android.R.drawable.ic_menu_compass, "Диагностика")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimaNavigation(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    Screen.Recovery,
                    Screen.Relax,
                    Screen.Profile,
                    Screen.Analytics,
                    Screen.Support,
                    Screen.Settings,
                    Screen.Diagnostic
                ).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(screen.icon), contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Recovery.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Recovery.route) { RecoveryScreen() }
            composable(Screen.Relax.route) { RelaxationScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Analytics.route) { AnalyticsScreen() }
            composable(Screen.Support.route) { SupportScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.Diagnostic.route) { DiagnosticScreen() }
        }
    }
} 