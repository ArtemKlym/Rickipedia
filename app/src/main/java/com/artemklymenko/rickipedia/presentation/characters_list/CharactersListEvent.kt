package com.artemklymenko.rickipedia.presentation.characters_list

sealed interface CharactersListEvent {
    data object GetInitialPage: CharactersListEvent
    data object GetNextPage: CharactersListEvent
}