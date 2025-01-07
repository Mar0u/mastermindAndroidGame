package com.example.mastermindAndroid.nav

sealed class Screen(val route: String) {
    object Login: Screen(route = "login")
    object Profile: Screen(route = "profile")
    object Game: Screen(route = "game")
    object Score: Screen(route = "score")
}