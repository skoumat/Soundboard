package cz.utb.fai.soundboard.navigation

sealed class Screen(val route: String) {
    data object Movies : Screen(Routes.MOVIES)

    data object EditMovie : Screen(Routes.EDIT_MOVIE){
        fun create(movieId: Long? = null): String {
            if (movieId != null) {
                return  "edit_movie/$movieId"
            }
            else return "edit_movie/-1"
        }
    }

    data object MovieDetails : Screen(Routes.MOVIE_DETAILS){
        fun create(movieName: String?): String {
            return  "movie_details?movieName=$movieName"
        }
    }



    data object Sounds : Screen(Routes.SOUNDS) {
        fun create(movieId: Long) = "sounds/$movieId"
    }

    data object EditSound : Screen(Routes.EDIT_SOUND){
        fun create(movieId: Long?, soundId: Long? = null): String {
            if (soundId != null) {
                return  "edit_sound/$movieId?soundId=$soundId"
            }
            else return "edit_sound/$movieId"
        }
    }
}