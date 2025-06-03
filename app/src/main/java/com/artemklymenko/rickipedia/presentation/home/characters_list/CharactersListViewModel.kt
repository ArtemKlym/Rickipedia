package com.artemklymenko.rickipedia.presentation.home.characters_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.network.models.domain.DomainCharacterPage
import com.artemklymenko.rickipedia.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(CharactersListState())
    val viewState = _viewState.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<DomainCharacterPage>()

    fun onEvent(event: CharactersListEvent) {
        when(event) {
            CharactersListEvent.GetInitialPage -> getInitialPage()
            is CharactersListEvent.GetNextPage -> getNextPage()
        }
    }

    private fun getInitialPage() {
        viewModelScope.launch {
            if(fetchedCharacterPages.isNotEmpty()) { return@launch }
            _viewState.update { it.copy( isLoading = true) }
            val initialPage = characterRepository.fetchCharactersPage(1)
            initialPage.onSuccess { page ->
                fetchedCharacterPages.clear()
                fetchedCharacterPages.add(page)
                _viewState.update { it.copy(
                        characters = page.characters,
                        isLoading = false
                    ) }
            }.onFailure { exception ->
                _viewState.update { it.copy(
                        errorMessage = exception.message,
                        isLoading = false
                    ) }
            }
        }
    }

    private fun getNextPage() {
        viewModelScope.launch {
            val nextPageIndex = fetchedCharacterPages.size + 1
            characterRepository.fetchCharactersPage(nextPageIndex)
                .onSuccess { page ->
                    val currentCharacters = _viewState.value.characters
                    fetchedCharacterPages.add(page)
                    _viewState.update { it.copy(
                        characters = currentCharacters + page.characters,
                        isLoading = false
                    ) }
                }.onFailure { exception ->
                    _viewState.update { it.copy(
                        errorMessage = exception.message,
                        isLoading = false
                    ) }
                }
        }
    }

}