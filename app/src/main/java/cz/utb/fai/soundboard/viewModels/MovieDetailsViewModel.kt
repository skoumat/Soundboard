package cz.utb.fai.soundboard.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import cz.utb.fai.soundboard.database.SoundboardRepository

class MovieDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SoundboardRepository) : ViewModel() {

    var movieNameMut by mutableStateOf(
        savedStateHandle["movieName"] ?: ""
    )

    var director by mutableStateOf("")
    var released by mutableStateOf("")
    var cast by mutableStateOf(listOf(""))

    init {
        val movieName = movieNameMut
        Log.e("XXXXXXXXXXX", "MovieName ${movieName}")

        if (movieName == ""){
            movieNameMut = "invalid movie name"
        }
        else{
            viewModelScope.launch {
                val movie = repository.fetchMoviesFromWiki(movieName).first()
                Log.e("TTTTTTTTTTTT", "${movie.title}")
                movieNameMut = movie.title
                director = movie.director ?: ""
                released = movie.releaseDate ?: ""
                cast = movie.cast
                Log.e("CAST SIZE", "${movie.cast.size}")
            }
        }
    }
}