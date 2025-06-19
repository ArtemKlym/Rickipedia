package com.artemklymenko.rickipedia.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavDestination(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    data object Home: NavDestination(title = "Home", route = "characters_list", Icons.Filled.Home)
    data object Episodes: NavDestination(title = "Episodes", route = "episodes", Icons.Filled.PlayArrow)
    data object Search: NavDestination(title = "Search", route = "search", Icons.Filled.Search)
    data object CharacterDetails : NavDestination(
        route = "character_details/{characterId}",
        title = "Details",
        icon = Icons.Filled.Info
    ) {
        fun createRoute(characterId: Int) = "character_details/$characterId"
    }
    data object CharacterEpisodes: NavDestination(
        route = "character_episodes",
        title = "Episodes",
        icon = Icons.Filled.Info
    )
}