package com.artemklymenko.rickipedia.presentation.search

import com.artemklymenko.network.models.domain.CharacterStatus

sealed interface SearchUiEvent {
    data class OnQueryChanged(val query: String) : SearchUiEvent
    data object OnSearchTriggered : SearchUiEvent
    data class OnStatusClick(val status: CharacterStatus): SearchUiEvent
}