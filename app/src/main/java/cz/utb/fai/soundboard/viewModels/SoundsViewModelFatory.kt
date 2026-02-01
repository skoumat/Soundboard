package cz.utb.fai.soundboard.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.utb.fai.soundboard.database.SoundboardRepository

class SoundsViewModelFatory(
    private val repository: SoundboardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoundsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SoundsViewModel(savedStateHandle, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}