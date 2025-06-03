package com.example.ahima.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.content.Context
import com.example.ahima.R

@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("profile", "relax", "tasks")

    Scaffold(
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Профиль") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("profile")
                    }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.SelfImprovement, contentDescription = null) },
                    label = { Text("Релакс") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("relax")
                    }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Задания") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("tasks")
                    }
                )
            }
        }
    ) {
        NavHost(navController, startDestination = "profile") {
            composable("profile") { ProfileScreen(context) }
            composable("relax") { Text("Релакс-зона (в разработке)") }
            composable("tasks") { Text("Задания (в разработке)") }
        }
    }
}
