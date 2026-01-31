package cz.utb.fai.soundboard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import cz.utb.fai.soundboard.views.MoviesScreen
import cz.utb.fai.soundboard.views.SoundsScreen

@Composable
fun SoundboardNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Movies.route
    ) {

        composable(Screen.Movies.route) {
            MoviesScreen(
                navController = navController,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Sounds.create(movieId))
                }
            )
        }

        composable(
            route = Screen.Sounds.route,
            arguments = listOf(navArgument("movieId") { type = NavType.LongType })
        ) {
            SoundsScreen()
        }

    }
}