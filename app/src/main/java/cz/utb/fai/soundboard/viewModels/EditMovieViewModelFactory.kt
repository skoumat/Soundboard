package cz.utb.fai.soundboard.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

import cz.utb.fai.soundboard.database.SoundboardRepository

class EditMovieViewModelFactory(
    private val repository: SoundboardRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle =
            extras.createSavedStateHandle()

        if (modelClass.isAssignableFrom(EditMovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditMovieViewModel(savedStateHandle, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}