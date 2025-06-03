package com.example.ahima.navigation

sealed class Route(val route: String) {
    object Daily : Route("daily")
    object Relax : Route("relax")
    object Settings : Route("settings")
    object Support : Route("support")
    object Report : Route("report")
}
