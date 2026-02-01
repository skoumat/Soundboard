package cz.utb.fai.soundboard.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.utb.fai.soundboard.views.EditMovieScreen
import cz.utb.fai.soundboard.views.EditSoundScreen

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
                    Log.e("CCCCCCCCCCCC", "MovieId = ${movieId}")
                    navController.navigate(Screen.Sounds.create(movieId))
                },
                onAddMovie = {
                    navController.navigate(Screen.EditMovie.route)
                }
            )
        }

        composable(
            route = Screen.EditMovie.route,
            arguments = listOf(navArgument("movieId") {
                type = NavType.LongType
                defaultValue = -1
            })
        ) { backStackEntry -> // TODO: lze udelat pres mutableLongStateOf viz SoundsViewModel

            val movieId = backStackEntry.arguments?.getLong("movieId")
            EditMovieScreen(
                movieId = movieId,
                navController = navController)
        }


        composable(
            route = Screen.Sounds.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.LongType
                })
        ) {
            SoundsScreen(
                navController = navController,
                onAddSound = { movieId ->
                    navController.navigate(Screen.EditSound.create(movieId = movieId))
                }
            )
        }

        composable(
            route = Screen.EditSound.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.LongType
                    defaultValue = -1
                },
                navArgument( "soundId") {
                    type = NavType.LongType
                    defaultValue = -1
            })
        ) { backStackEntry -> // TODO: lze udelat pres mutableLongStateOf viz SoundsViewModel

            val soundId = backStackEntry.arguments?.getLong("soundId")
            EditSoundScreen(
                soundId = soundId,
                navController = navController)
        }
    }
}