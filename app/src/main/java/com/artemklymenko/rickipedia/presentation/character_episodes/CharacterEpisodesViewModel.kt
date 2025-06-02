package com.artemklymenko.rickipedia.presentation.character_episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.rickipedia.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterEpisodesViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {
    private val _internalStorageState = MutableStateFlow(CharacterEpisodesState())
    val stateFlow = _internalStorageState.asStateFlow()

    fun onEvent(event: CharacterEpisodesEvent) {
        when(event){
            is CharacterEpisodesEvent.GetCharacterEpisodes -> getCharacterEpisodes(event.character)
        }
    }

    private fun getCharacterEpisodes(character: DomainCharacter) = viewModelScope.launch {
        _internalStorageState.update {
            it.copy(isLoading = true)
        }
        characterRepository.fetchCharacterEpisodes(character.episodeIds)
            .onSuccess { episodes ->
                _internalStorageState.update {
                    it.copy(
                        episodes = episodes,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _internalStorageState.update {
                    it.copy(
                        errorMessage = exception.message,
                        isLoading = false
                    )
                }
            }
    }
}