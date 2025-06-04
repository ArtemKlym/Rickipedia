package com.artemklymenko.rickipedia.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.network.models.domain.CharacterStatus
import com.artemklymenko.rickipedia.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        observeDebouncedQuery()
    }

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnQueryChanged -> {
                _uiState.update { it.copy(query = event.query) }
                queryFlow.value = event.query
            }
            is SearchUiEvent.OnSearchTriggered -> {
                searchCharacters(_uiState.value.query)
            }
            is SearchUiEvent.OnStatusClick -> toggleStatus(event.status)
        }
    }

    private fun toggleStatus(status: CharacterStatus) {
        _uiState.update { current ->
            val filtered = current.allResults.filter { it.status == status }
            current.copy(
                results = filtered,
                filterState = current.filterState?.copy(selectedStatus = status)
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeDebouncedQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(700)
                .distinctUntilChanged()
                .collectLatest {
                    onEvent(SearchUiEvent.OnSearchTriggered)
                }
        }
    }

    private fun searchCharacters(query: String) {
        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    allResults = emptyList(),
                    results = emptyList(),
                    filterState = null,
                    errorMessage = null,
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            characterRepository.fetchAllCharactersByName(query)
                .onSuccess { characters ->
                    val uniqueStatuses = characters.map { it.status }.distinct()
                    val defaultStatus = uniqueStatuses.firstOrNull() ?: CharacterStatus.Unknown
                    val filteredResults = characters.filter { it.status == defaultStatus }

                    _uiState.update {
                        it.copy(
                            allResults = characters,
                            results = filteredResults,
                            filterState = SearchUiState.FilterState(
                                statuses = uniqueStatuses.sortedBy { s -> s.displayName },
                                selectedStatus = defaultStatus
                            ),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No search results found"
                        )
                    }
                }
        }
    }
}

