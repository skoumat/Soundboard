package cz.utb.fai.soundboard.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cz.utb.fai.soundboard.models.Sound

enum class SortingOrder {
    ASC, DESC
}

class SoundsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var searchQuery by mutableStateOf(savedStateHandle["searchQuery"] ?: "")

    var sortOrder by mutableStateOf(savedStateHandle["sortOrder"] ?: SortingOrder.ASC)

    var characterFilter by mutableStateOf(savedStateHandle["characterFilter"] as String?)

    private val testSounds = listOf(
        Sound(1, "Proc se na me tak divas", "Blue", ""),
        Sound(2, "Ahooj", "Kate", ""),
        Sound(3, "Omlouvam se", "Norbit", ""),
        Sound(3, "To vtip", "Wong", "")
    )

    val characters: List<String>
        get() = testSounds.map { it.character }.distinct()

    val filteredSounds: List<Sound>
        get() {
            var list = testSounds

            if (searchQuery.isNotBlank()) {
                list = list.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }

            characterFilter?.let { character ->
                list = list.filter { it.character == character }
            }

            list = when (sortOrder) {
                SortingOrder.ASC -> list.sortedBy { it.name }
                SortingOrder.DESC -> list.sortedByDescending { it.name }
            }

            return list
        }

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
}