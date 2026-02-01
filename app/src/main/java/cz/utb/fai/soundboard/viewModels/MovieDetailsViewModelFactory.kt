package cz.utb.fai.soundboard.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

import cz.utb.fai.soundboard.database.SoundboardRepository

class MovieDetailsViewModelFactory(
    private val repository: SoundboardRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle =
            extras.createSavedStateHandle()

        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(savedStateHandle, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}