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
import cz.utb.fai.soundboard.services.api.MovieDetailsModel

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

        if (movieName == ""){
            movieNameMut = "invalid movie name"
        }
        else{
            viewModelScope.launch {
                val movies = repository.fetchMoviesFromWiki(movieName)
                var movie: MovieDetailsModel? = null
                if (movies.isNotEmpty()){
                    movie = movies.first()
                }

                if (movie != null){
                    movieNameMut = movie.title
                    director = movie.director ?: ""
                    released = movie.releaseDate ?: ""
                    cast = movie.cast
                }
                else{
                    movieNameMut = "no such movie"
                    director = ""
                    released = ""
                    cast = emptyList()
                }
            }
        }
    }
}