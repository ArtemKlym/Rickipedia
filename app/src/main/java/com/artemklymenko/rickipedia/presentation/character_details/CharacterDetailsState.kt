package com.artemklymenko.rickipedia.presentation.character_details

import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.rickipedia.core.domain.DataPoint

data class CharacterDetailsState(
    val character: DomainCharacter? = null,
    val dataPoints: List<DataPoint> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
