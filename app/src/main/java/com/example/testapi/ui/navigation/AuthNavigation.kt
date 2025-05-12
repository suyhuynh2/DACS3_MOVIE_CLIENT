//package com.example.testapi.ui.navigation
//
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import com.example.testapi.ui.screens.intro.IntroScreen
//import com.example.testapi.ui.screens.login.LoginScreen
//
//fun NavGraphBuilder.authNavigation(navController: NavHostController) {
//    composable("intro") {
//        IntroScreen(
//            onStartClicked = {
//                navController.navigate("main") {
//                    popUpTo("intro") { inclusive = true }
//                }
//            }
//        )
//    }
//    composable("login") {
//        LoginScreen(
//            onLoginSuccess = {
//                navController.navigate("main") {
//                    popUpTo("login") { inclusive = true }
//                }
//            }
//        )
//    }
//}