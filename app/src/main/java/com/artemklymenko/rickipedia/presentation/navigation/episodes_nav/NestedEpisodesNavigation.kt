package com.artemklymenko.rickipedia.presentation.navigation.episodes_nav

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.artemklymenko.rickipedia.presentation.episodes.AllEpisodesScreen
import com.artemklymenko.rickipedia.presentation.episodes.AllEpisodesViewModel
import com.artemklymenko.rickipedia.presentation.navigation.NavDestination

fun NavGraphBuilder.episodesGraph(
    navHostController: NavHostController,
    modifier: Modifier
) {
    composable(NavDestination.Episodes.route) {
        val viewModel = hiltViewModel<AllEpisodesViewModel>()
        val state = viewModel.uiState.collectAsState()
        AllEpisodesScreen(
            state = state,
            modifier = modifier,
            onEvent = viewModel::onEvent
        )
    }
}