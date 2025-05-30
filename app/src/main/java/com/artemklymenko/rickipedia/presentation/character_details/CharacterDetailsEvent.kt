package com.artemklymenko.rickipedia.presentation.character_details

sealed interface CharacterDetailsEvent {
    data class GetCharacterDetails(val characterId: Int): CharacterDetailsEvent
}