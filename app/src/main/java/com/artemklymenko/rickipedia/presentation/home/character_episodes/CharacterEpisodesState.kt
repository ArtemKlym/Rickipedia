package com.artemklymenko.rickipedia.presentation.home.character_episodes

import com.artemklymenko.network.models.domain.DomainEpisode

data class CharacterEpisodesState(
    val episodes: List<DomainEpisode> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
