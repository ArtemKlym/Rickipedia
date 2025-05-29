package com.artemklymenko.rickipedia.presentation.characters_list

import com.artemklymenko.network.models.domain.DomainCharacter

data class CharactersListState(
    val characters: List<DomainCharacter> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
