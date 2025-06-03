package com.artemklymenko.rickipedia.presentation.episodes

sealed interface AllEpisodesEvent {
    data object GetAllEpisodes: AllEpisodesEvent
}