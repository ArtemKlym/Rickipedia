package com.artemklymenko.rickipedia.presentation.character_details

import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.rickipedia.core.domain.DataPoint

sealed interface CharacterDetailsEvent {
    data class GetCharacterDetails(val characterId: Int): CharacterDetailsEvent
}