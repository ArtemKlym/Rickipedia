package com.artemklymenko.network.models.domain

sealed class CharacterStatus(val displayName: String) {
    data object Alive: CharacterStatus("Alive")
    data object Dead: CharacterStatus("Dead")
    data object Unknown: CharacterStatus("Unknown")
}