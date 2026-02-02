package cz.utb.fai.soundboard.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import cz.utb.fai.soundboard.database.SoundboardRepository
import cz.utb.fai.soundboard.domainModels.MovieModel
import cz.utb.fai.soundboard.domainModels.SoundModel


enum class SortingOrder {
    ASC, DESC
}

class SoundsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SoundboardRepository,
) : ViewModel() {

    var movieId by mutableLongStateOf(
        savedStateHandle.get<Long>("movieId") ?: -1L
    )

    var movieName by mutableStateOf( "")

    var movie by mutableStateOf<MovieModel?>(null)

        init {
            val movieId = movieId
            if (movieId >= 0){
                viewModelScope.launch {
                    movie = repository.getMovie(movieId)
                    movieName = movie!!.name
                }
            }
        }


    var searchQuery by mutableStateOf(savedStateHandle["searchQuery"] ?: "")

    var sortOrder by mutableStateOf(savedStateHandle["sortOrder"] ?: SortingOrder.ASC)

    var characterFilter by mutableStateOf(savedStateHandle["characterFilter"] as String?)


    private val allMovieSoundsFlow: Flow<List<SoundModel>> = repository.getAllSoundsFlow(movieId)

    val filteredSounds: StateFlow<List<SoundModel>> = combine(
        allMovieSoundsFlow,
        snapshotFlow { searchQuery },
        snapshotFlow { characterFilter },
        snapshotFlow { sortOrder }
    ) { sounds, query, character, order ->
        var list = sounds

        if (query.isNotBlank()) {
            list = list.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        character?.let {
            list = list.filter {
                sound -> sound.characters.contains(it) }
        }

        list = when (order) {
            SortingOrder.ASC -> list.sortedBy { it.name.lowercase() }
            SortingOrder.DESC -> list.sortedByDescending { it.name.lowercase() }
        }

        list
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    val characters: StateFlow<List<String>> =
        repository.getMovieCharacters(movieId).stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
            )

    fun onSearchChange(value: String) {
        searchQuery = value
        savedStateHandle["searchQuery"] = value
    }

    fun onSortChange(order: SortingOrder) {
        sortOrder = order
        savedStateHandle["sortOrder"] = order
    }

    fun onCharacterFilter(character: String?) {
        characterFilter = character
        savedStateHandle["characterFilter"] = character
    }


    fun deleteSound(soundId: Long){
        viewModelScope.launch {
            repository.deleteSound(soundId)
        }
    }
}