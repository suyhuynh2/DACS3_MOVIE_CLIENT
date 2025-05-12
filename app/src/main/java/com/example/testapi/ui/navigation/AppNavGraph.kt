//package com.example.testapi.ui.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//
//@Composable
//fun AppNavGraph(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//    NavHost(
//        navController = navController,
//        startDestination = "main",
//        modifier = modifier
//    ) {
//        // Navigation cho các màn hình xác thực
//        authNavigation(navController)
//
//        // Navigation cho các màn hình chính
//        mainNavigation(navController)
//
//        // Navigation cho các màn hình chi tiết
//        detailNavigation(navController)
//    }
//}