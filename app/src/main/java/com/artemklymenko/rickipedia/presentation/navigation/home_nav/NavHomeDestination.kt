package com.artemklymenko.rickipedia.presentation.navigation.home_nav

sealed class NavHomeDestination(
    val route: String
) {
    data object CharacterDetails : NavHomeDestination(
        route = "character_details/{characterId}"
    ) {
        fun createRoute(characterId: Int) = "character_details/$characterId"
    }
    data object CharacterEpisodes: NavHomeDestination(route = "character_episodes")
}