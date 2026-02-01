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
import cz.utb.fai.soundboard.domainModels.MovieModel


class EditMovieViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SoundboardRepository
) : ViewModel() {

    private var movieIdMutable by mutableStateOf<Long?>(savedStateHandle["movieId"])

    var movie by mutableStateOf<MovieModel?>(null)



    var movieName by mutableStateOf(
        savedStateHandle["movieName"] ?: ""
    )

    var movieCharacters by mutableStateOf(
        savedStateHandle["characters"] ?: emptyList<String>()
    )

    init {
        val movieId = movieIdMutable
        Log.e("XXXXXXXXXXX", "MovieId ${movieId}")
        if (movieId != null && movieId >= 0){
            viewModelScope.launch {
                movie = repository.getMovie(movieId)
                Log.e("TTTTTTTTTTTT", "${movie!!.name}")
                movieName = movie!!.name
                movieCharacters = movie!!.characters
            }
        }
    }

    var newCharacter by mutableStateOf("")

    fun onMovieNameChange(value: String) {
        movieName = value
        savedStateHandle["movieName"] = value
    }

    fun onNewCharacterChange(value: String) {
        newCharacter = value
    }

    fun addCharacter() {
        if (newCharacter.isNotBlank()) {
            movieCharacters = movieCharacters + newCharacter
            savedStateHandle["characters"] = movieCharacters
            newCharacter = ""
        }
    }

    fun removeCharacter(character: String) {
        movieCharacters = movieCharacters - character
        savedStateHandle["characters"] = movieCharacters
    }

    fun deleteMovie() {
        viewModelScope.launch {
            val movieId = movieIdMutable
            if (movieId != null){
                repository.deleteMovie(movieId)
            }
        }
    }

    fun saveMovie() {
        viewModelScope.launch {
            val movieId = movieIdMutable
            if (movieId != null && movieId >= 0){
                repository.updateMovie(MovieModel(id = movieId, name = movieName, characters = movieCharacters))
            }
            else{
                val newMovie = MovieModel(id = null, name = movieName, characters = movieCharacters)
                repository.addMovie(newMovie)
                movieIdMutable = newMovie.id
                // TODO: toto nefunguje bez entity
            }
        }
    }
}