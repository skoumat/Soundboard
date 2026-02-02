package cz.utb.fai.soundboard.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

import cz.utb.fai.soundboard.database.SoundboardRepository
import cz.utb.fai.soundboard.domainModels.SoundModel

class EditSoundViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SoundboardRepository,
) : ViewModel() {

    val movieId: Long =
        checkNotNull(savedStateHandle.get<Long>("movieId"))

    private var soundIdMutable by mutableStateOf<Long?>(savedStateHandle["soundId"])

    var sound by mutableStateOf<SoundModel?>(null)

    init {
        val soundId = soundIdMutable
        if (soundId != null && soundId >= 0){
            viewModelScope.launch {
                sound = repository.getSound(soundId)
                soundName = sound!!.name
                selectedCharacter = sound!!.characters.first()
                soundFileUriString = sound!!.filePathString
            }
        }
    }

    var soundName by mutableStateOf(savedStateHandle["soundName"] ?: "")
        private set

//    var characters by mutableStateOf<List<String>>(savedStateHandle["selectedCharacter"] ?: emptyList())
//        private set

    var selectedCharacter by mutableStateOf<String>(savedStateHandle["selectedCharacter"] ?: "")
       private set

    var soundFileUriString by mutableStateOf<String?>(savedStateHandle["soundFileUriString"])
        private set

    val characters: StateFlow<List<String>> = (
                movieId.let {
                    repository.getMovieCharacters(movieId)
                }
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

    fun soundSelected(uri: String) {
        soundFileUriString = uri
    }

    fun saveSound() {
        viewModelScope.launch {
            val soundId = soundIdMutable
            val soundFileUriStringInner = soundFileUriString
            val selectedCharacterInner = selectedCharacter
            if (soundFileUriStringInner != null){
                if (soundId != null && soundId >= 0){
                    repository.updateSound(
                        SoundModel(
                            id = soundId,
                            movieId = movieId,
                            filePathString = soundFileUriStringInner,
                            name = soundName,
                            characters = listOf(selectedCharacterInner),
                        ))
                }
                else{
                    val newSound = SoundModel(
                        id = null,
                        movieId = movieId,
                        filePathString = soundFileUriStringInner,
                        name = soundName,
                        characters = listOf(selectedCharacterInner)
                    )

                    soundIdMutable = repository.addSound(newSound)
                }
            }

        }

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