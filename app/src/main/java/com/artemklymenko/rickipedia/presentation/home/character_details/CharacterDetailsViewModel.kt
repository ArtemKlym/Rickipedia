package com.artemklymenko.rickipedia.presentation.home.character_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.rickipedia.core.domain.DataPoint
import com.artemklymenko.rickipedia.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {

    private val _internalStorageFlow = MutableStateFlow(CharacterDetailsState())
    val stateFlow = _internalStorageFlow.asStateFlow()

    fun onEvent(uiEvent: CharacterDetailsEvent){
        when(uiEvent) {
            is CharacterDetailsEvent.GetCharacterDetails -> fetchCharacter(uiEvent.characterId)
        }
    }



    private fun fetchCharacter(characterId: Int) {
        viewModelScope.launch {
            _internalStorageFlow.update { it.copy(
                isLoading = true
            ) }
            characterRepository.fetchCharacter(characterId)
                .onSuccess { character ->
                    val dataPoints = buildList {
                        add(DataPoint("Last known location", character.location.name))
                        add(DataPoint("Species", character.species))
                        add(DataPoint("Gender", character.gender.displayName))
                        character.type.takeIf { it.isNotEmpty() }?.let { type ->
                            add(DataPoint("Type", type))
                        }
                        add(DataPoint("Origin", character.origin.name))
                        add(DataPoint("Episode count", character.episodeIds.size.toString()))
                    }
                    _internalStorageFlow.update {
                        it.copy(
                            character = character,
                            dataPoints = dataPoints,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _internalStorageFlow.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                        )
                    }
                }
        }
    }
}