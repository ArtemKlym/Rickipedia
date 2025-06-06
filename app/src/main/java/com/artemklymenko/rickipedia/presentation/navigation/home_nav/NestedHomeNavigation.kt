package com.artemklymenko.rickipedia.presentation.navigation.home_nav

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.artemklymenko.rickipedia.presentation.home.character_details.CharacterDetailsScreen
import com.artemklymenko.rickipedia.presentation.home.character_details.CharacterDetailsViewModel
import com.artemklymenko.rickipedia.presentation.home.character_episodes.CharacterEpisodesScreen
import com.artemklymenko.rickipedia.presentation.home.character_episodes.CharacterEpisodesViewModel
import com.artemklymenko.rickipedia.presentation.home.characters_list.CharactersListScreen
import com.artemklymenko.rickipedia.presentation.home.characters_list.CharactersListViewModel
import com.artemklymenko.rickipedia.presentation.navigation.NavDestination
import com.artemklymenko.rickipedia.presentation.navigation.SharedViewModel

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
                navController.navigate(NavHomeDestination.CharacterDetails.createRoute(characterId))
            }
        )
    }

    composable(
        route = NavHomeDestination.CharacterDetails.route,
        arguments = listOf(navArgument("characterId") { type = NavType.IntType })
    ) { backStackEntry ->
        val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
        val viewModel = hiltViewModel<CharacterDetailsViewModel>()
        val state = viewModel.stateFlow
        val parentEntry = remember(navController.currentBackStackEntry) {
            navController.getBackStackEntry(NavDestination.Home.route)
        }
        val sharedViewModel = hiltViewModel<SharedViewModel>(parentEntry)
        CharacterDetailsScreen(
            characterId = characterId,
            modifier = modifier,
            stateFlow = state,
            onEvent = viewModel::onEvent,
            onEpisodeClick = { character ->
                sharedViewModel.selectedCharacter = character
                navController.navigate(NavHomeDestination.CharacterEpisodes.route)
            },
            onBackClick = {
                navController.navigateUp()
            }
        )
    }
    composable(route = NavHomeDestination.CharacterEpisodes.route) {
        val parentEntry = remember(navController.currentBackStackEntry) {
            navController.getBackStackEntry(NavDestination.Home.route)
        }
        val sharedViewModel = hiltViewModel<SharedViewModel>(parentEntry)
        val character = sharedViewModel.selectedCharacter
        character?.let {
            val viewModel = hiltViewModel<CharacterEpisodesViewModel>()
            val state = viewModel.stateFlow.collectAsState()
            CharacterEpisodesScreen(
                character = it,
                state = state,
                modifier = modifier,
                onEvent = viewModel::onEvent,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}