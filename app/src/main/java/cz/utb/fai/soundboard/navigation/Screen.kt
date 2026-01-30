package cz.utb.fai.soundboard.navigation

sealed class Screen(val route: String) {
    data object Movies : Screen("movies")
}