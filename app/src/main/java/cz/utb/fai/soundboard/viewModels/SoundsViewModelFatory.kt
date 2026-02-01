package cz.utb.fai.soundboard.viewModels

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.savedstate.SavedStateRegistryOwner
import cz.utb.fai.soundboard.database.SoundboardRepository

class SoundsViewModelFatory(
    private val repository: SoundboardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(SoundsViewModel::class.java)) {
            val savedStateHandle =
                extras.createSavedStateHandle()

            @Suppress("UNCHECKED_CAST")
            return SoundsViewModel(savedStateHandle, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}