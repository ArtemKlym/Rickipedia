package com.artemklymenko.rickipedia.presentation.character_episodes

import com.artemklymenko.network.models.domain.DomainCharacter

sealed interface CharacterEpisodesEvent {
    data class GetCharacterEpisodes(val character: DomainCharacter): CharacterEpisodesEvent
}