package cz.utb.fai.soundboard.navigation

object Routes {
    const val MOVIES = "movies"
    const val SOUNDS = "sounds/{movieId}"
    const val EDIT_MOVIE = "edit_movie/{movieId}"
    const val EDIT_SOUND = "edit_sound/{movieId}?soundId={soundId}"
}