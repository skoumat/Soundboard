package cz.utb.fai.soundboard.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import cz.utb.fai.soundboard.database.SoundboardRepository

class MoviesViewModelFactory(
    private val repository: SoundboardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesViewModel(savedStateHandle, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}