package com.artemklymenko.rickipedia.presentation.navigation.home_nav

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.artemklymenko.rickipedia.presentation.home.characters_list.CharactersListScreen
import com.artemklymenko.rickipedia.presentation.home.characters_list.CharactersListViewModel
import com.artemklymenko.rickipedia.presentation.navigation.NavDestination

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    composable(NavDestination.Home.route) {
        val viewModel = hiltViewModel<CharactersListViewModel>()
        val state = viewModel.viewState.collectAsState()
        CharactersListScreen(
            modifier = modifier,
            state = state,
            onEvent = viewModel::onEvent,
            onCharacterClick = { characterId ->
                navController.navigate(NavDestination.CharacterDetails.createRoute(characterId))
            }
        )
    }
}