package com.artemklymenko.rickipedia.presentation.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.rickipedia.data.repository.EpisodesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEpisodesViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllEpisodesState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AllEpisodesEvent) {
        when (event) {
            AllEpisodesEvent.GetAllEpisodes -> getAllEpisodes()
        }
    }

    private fun getAllEpisodes() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            episodesRepository.fetchAllEpisodes()
                .onSuccess { episodeList ->
                    _uiState.update { allEpisodesState ->
                        allEpisodesState.copy(
                            isLoading = false,
                            episodes = episodeList.groupBy {
                                it.seasonNumber.toString()
                            }.mapKeys {
                                "Season ${it.key}"
                            }
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }
}