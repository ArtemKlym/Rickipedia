package com.artemklymenko.rickipedia.presentation.episodes

import com.artemklymenko.network.models.domain.DomainEpisode

data class AllEpisodesState(
    val episodes: Map<String, List<DomainEpisode>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)
