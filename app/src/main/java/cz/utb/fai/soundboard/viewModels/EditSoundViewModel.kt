package cz.utb.fai.soundboard.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import cz.utb.fai.soundboard.database.SoundboardRepository
import cz.utb.fai.soundboard.domainModels.SoundModel

class EditSoundViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SoundboardRepository,
) : ViewModel() {

    private var soundIdMutable by mutableStateOf<Long?>(savedStateHandle["soundId"])

    var sound by mutableStateOf<SoundModel?>(null)

    init {
        val soundId = soundIdMutable
        if (soundId != null){
            viewModelScope.launch {
                sound = repository.getSound(soundId)
            }
        }
    }

    var soundName by mutableStateOf(savedStateHandle["soundName"] ?: "")
        private set

    var selectedCharacter by mutableStateOf<String?>(savedStateHandle["selectedCharacter"])
        private set

    var soundFileName by mutableStateOf<String?>(savedStateHandle["soundFileName"])
        private set

    val characters: StateFlow<List<String>> = (
                soundIdMutable?.let { soundId ->
                    repository.getSoundCharacters(soundId)
                } ?: flowOf(emptyList())
                ).stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
    )


    fun onSoundNameChange(value: String) {
        soundName = value
        savedStateHandle["soundName"] = value
    }

    fun onCharacterSelected(character: String) {
        selectedCharacter = character
        savedStateHandle["selectedCharacter"] = character
    }

    fun selectSoundFile() {
        // TODO: ActivityResultContracts?
    }

    fun saveSound() {
        // TODO:
    }

    fun deleteSound() {
        val soundId = soundIdMutable
        if (soundId != null){
            viewModelScope.launch {
                repository.deleteSound(soundId)
            }
        }
    }
}