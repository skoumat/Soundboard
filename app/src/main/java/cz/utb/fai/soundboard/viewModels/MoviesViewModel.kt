package cz.utb.fai.soundboard.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.combine

import cz.utb.fai.soundboard.database.SoundboardRepository
import cz.utb.fai.soundboard.domainModels.MovieModel

class MoviesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SoundboardRepository
) : ViewModel() {
    var searchQuery by mutableStateOf(
        savedStateHandle["searchQuery"] ?: ""
    )
        private set

    private val allMoviesFlow: Flow<List<MovieModel>> = repository.getAllMoviesFlow()

    val filteredMovies: StateFlow<List<MovieModel>> = combine(
        allMoviesFlow,
        snapshotFlow { searchQuery }
    ) { movies, query ->
        if (query.isBlank()) movies
        else movies.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    fun onSearchChange(value: String) {
        searchQuery = value
        savedStateHandle["searchQuery"] = value
    }

    fun deleteMovie(movieId: Long){
        viewModelScope.launch {
            repository.deleteMovie(movieId)
        }
    }
}