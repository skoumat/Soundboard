package cz.utb.fai.soundboard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import cz.utb.fai.soundboard.views.MoviesScreen

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
            )
        }

    }
}