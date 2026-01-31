package cz.utb.fai.soundboard.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cz.utb.fai.soundboard.models.Movie

class MoviesViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var searchQuery by mutableStateOf(
        savedStateHandle["searchQuery"] ?: ""
    )
        private set

    private val testMovies = listOf(
        Movie(1, "Pulp Fiction", mutableListOf("A", "B")),
        Movie(2, "The Matrix", mutableListOf("Neo", "Trinity")),
        Movie(3, "Inception", mutableListOf("DiCaprio", "Zena")),
        Movie(4, "Interstellar", mutableListOf("Otec", "Holka"))
    )

    val filteredMovies: List<Movie>
        get() = testMovies.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }

    fun onSearchChange(value: String) {
        searchQuery = value
        savedStateHandle["searchQuery"] = value
    }
}