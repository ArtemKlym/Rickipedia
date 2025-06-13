package com.artemklymenko.rickipedia.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.artemklymenko.rickipedia.presentation.home.character_details.CharacterDetailsScreen
import com.artemklymenko.rickipedia.presentation.home.character_details.CharacterDetailsViewModel
import com.artemklymenko.rickipedia.presentation.home.character_episodes.CharacterEpisodesScreen
import com.artemklymenko.rickipedia.presentation.home.character_episodes.CharacterEpisodesViewModel
import com.artemklymenko.rickipedia.presentation.navigation.episodes_nav.episodesGraph
import com.artemklymenko.rickipedia.presentation.navigation.home_nav.homeGraph
import com.artemklymenko.rickipedia.presentation.navigation.search_nav.searchGraph

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.Home.route
    ) {
        homeGraph(
            navController = navController,
            modifier = modifier,
        )
        episodesGraph(
            navHostController = navController,
            modifier = modifier
        )
        searchGraph(
            navController = navController,
            modifier = modifier
        )
        composable(
            route = NavDestination.CharacterDetails.route,
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
                    navController.navigate(NavDestination.CharacterEpisodes.route)
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable(route = NavDestination.CharacterEpisodes.route) {
            val parentEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(NavDestination.Home.route)
            }
            val sharedViewModel = hiltViewModel<SharedViewModel>(parentEntry)

            val character = sharedViewModel.selectedCharacter
            character?.let {
                val viewModel = hiltViewModel<CharacterEpisodesViewModel>()
                val state = viewModel.stateFlow.collectAsState()
                CharacterEpisodesScreen(
                    character = character,
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
}