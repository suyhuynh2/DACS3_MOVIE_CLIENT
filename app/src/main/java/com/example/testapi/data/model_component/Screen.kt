package com.example.testapi.data.model_component

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object MenuMovie : Screen("menu_movie")
}