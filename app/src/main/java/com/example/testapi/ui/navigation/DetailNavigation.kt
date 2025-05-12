//package com.example.testapi.ui.navigation
//
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.composable
//import androidx.navigation.navArgument
//import com.example.testapi.ui.screens.detailmovie.DetailMovieScreen
//import com.example.testapi.ui.screens.favmovie.ListFavoriteMovieScreen
//import com.example.testapi.ui.screens.myinfo.MyInfoScreen
//
//
//fun NavGraphBuilder.detailNavigation(navController: NavHostController) {
//    composable(
//        route = "detail_movie/{movie_id}/{title}/{trailer_url}/{video_url}/{status}/{averageRating}/{description}/{duration}/{actors}/{genres}/{poster_url}",
//        arguments = listOf(
//            navArgument("movie_id") { type = NavType.IntType },
//            navArgument("title") { type = NavType.StringType; nullable = true },
//            navArgument("trailer_url") { type = NavType.StringType; nullable = true },
//            navArgument("video_url") { type = NavType.StringType; nullable = true },
//            navArgument("status") { type = NavType.StringType; nullable = true },
//            navArgument("averageRating") { type = NavType.FloatType },
//            navArgument("description") { type = NavType.StringType; nullable = true },
//            navArgument("duration") { type = NavType.IntType },
//            navArgument("actors") { type = NavType.StringType; nullable = true },
//            navArgument("genres") { type = NavType.StringType; nullable = true },
//            navArgument("poster_url") { type = NavType.StringType; nullable = true }
//        )
//    ) { backStackEntry ->
//        DetailMovieScreen(
//            movie_id = backStackEntry.arguments?.getInt("movie_id") ?: 0,
//            title = backStackEntry.arguments?.getString("title"),
//            trailer_url = backStackEntry.arguments?.getString("trailer_url"),
//            video_url = backStackEntry.arguments?.getString("video_url"),
//            status = backStackEntry.arguments?.getString("status"),
//            averageRating = backStackEntry.arguments?.getFloat("averageRating"),
//            description = backStackEntry.arguments?.getString("description"),
//            duration = backStackEntry.arguments?.getInt("duration"),
//            actors = backStackEntry.arguments?.getString("actors"),
//            genres = backStackEntry.arguments?.getString("genres"),
//            poster_url = backStackEntry.arguments?.getString("poster_url"),
//            onBack = { navController.popBackStack() }
//        )
//    }
//
//    composable(
//        route = "my_info/{user_id}/{user_name}/{user_email}/{user_role}/{user_provider}",
//        arguments = listOf(
//            navArgument("user_id") { type = NavType.StringType },
//            navArgument("user_name") { type = NavType.StringType },
//            navArgument("user_email") { type = NavType.StringType },
//            navArgument("user_role") { type = NavType.StringType },
//            navArgument("user_provider") { type = NavType.StringType }
//        )
//    ) { backStackEntry ->
//        MyInfoScreen(
//            firebaseUid = backStackEntry.arguments?.getString("user_id") ?: "",
//            initialUsername = backStackEntry.arguments?.getString("user_name") ?: "",
//            email = backStackEntry.arguments?.getString("user_email") ?: "",
//            role = backStackEntry.arguments?.getString("user_role") ?: "",
//            loginProvider = backStackEntry.arguments?.getString("user_provider") ?: "",
//            onBack = { navController.popBackStack() }
//        )
//    }
//
//    composable("list_favorite") {
//        ListFavoriteMovieScreen(
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
//            onBack = { navController.popBackStack() }
//        )
//    }
//}