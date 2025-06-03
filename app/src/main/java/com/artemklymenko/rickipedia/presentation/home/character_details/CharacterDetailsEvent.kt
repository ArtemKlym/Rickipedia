package com.artemklymenko.rickipedia.presentation.home.character_details

sealed interface CharacterDetailsEvent {
    data class GetCharacterDetails(val characterId: Int): CharacterDetailsEvent
}