//package com.example.testapi.ui.navigation
//
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import com.example.testapi.ui.screens.screen.MainScreen
//import com.example.testapi.data.mode_data.Movie
//
//fun NavGraphBuilder.mainNavigation(navController: NavHostController) {
//    composable("main") {
//        MainScreen(
//            onNavigateToDetail = { movie ->
//                navController.navigate(
//                    "detail_movie/" +
//                            "${movie.movie_id}/" +
//                            "${movie.title}/" +
//                            "${movie.trailer_url}/" +
//                            "${movie.video_url}/" +
//                            "${movie.status}/" +
//                            "${movie.averageRating}/" +
//                            "${movie.description}/" +
//                            "${movie.duration}/" +
//                            "${movie.actors}/" +
//                            "${movie.genres?.joinToString(",") { it.name }}/" +
//                            "${movie.poster_url}"
//                )
//            },
//            onNavigateToMyInfo = { userData ->
//                navController.navigate(
//                    "my_info/" +
//                            "${userData["user_id"]}/" +
//                            "${userData["user_name"]}/" +
//                            "${userData["user_email"]}/" +
//                            "${userData["user_role"]}/" +
//                            "${userData["user_provider"]}"
//                )
//            },
//            onNavigateToFavorite = {
//                navController.navigate("list_favorite")
//            },
//            onNavigateToLogin = {
//                navController.navigate("login") {
//                    popUpTo("main") { inclusive = true }
//                }
//            }
//        )
//    }
//}