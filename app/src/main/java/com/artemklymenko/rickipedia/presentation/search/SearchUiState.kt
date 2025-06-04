package com.artemklymenko.rickipedia.presentation.search

import com.artemklymenko.network.models.domain.CharacterStatus
import com.artemklymenko.network.models.domain.DomainCharacter

data class SearchUiState(
    val query: String = "",
    val allResults: List<DomainCharacter> = emptyList(), // unfiltered list
    val results: List<DomainCharacter> = emptyList(), // filtered list
    val filterState: FilterState? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    data class FilterState(
        val statuses: List<CharacterStatus>,
        val selectedStatus: CharacterStatus
    )
}
