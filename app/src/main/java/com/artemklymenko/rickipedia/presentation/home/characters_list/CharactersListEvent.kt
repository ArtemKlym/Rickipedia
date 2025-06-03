package com.artemklymenko.rickipedia.presentation.home.characters_list

sealed interface CharactersListEvent {
    data object GetInitialPage: CharactersListEvent
    data object GetNextPage: CharactersListEvent
}