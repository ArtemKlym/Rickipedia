package com.artemklymenko.rickipedia.presentation.navigation.search_nav

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.artemklymenko.rickipedia.presentation.navigation.NavDestination
import com.artemklymenko.rickipedia.presentation.search.SearchScreen
import com.artemklymenko.rickipedia.presentation.search.SearchViewModel

fun NavGraphBuilder.searchGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    composable(route = NavDestination.Search.route) {
        val viewModel: SearchViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        SearchScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            modifier = modifier,
            onCharacterClick = { characterId ->
               navController.navigate(NavDestination.CharacterDetails.createRoute(characterId))
            }
        )
    }
}