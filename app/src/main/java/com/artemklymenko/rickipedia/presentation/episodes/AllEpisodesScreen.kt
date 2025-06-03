package com.artemklymenko.rickipedia.presentation.episodes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemklymenko.network.models.domain.DomainEpisode
import com.artemklymenko.rickipedia.core.components.ErrorMessageComponent
import com.artemklymenko.rickipedia.core.components.LoadingState
import com.artemklymenko.rickipedia.presentation.home.character_episodes.components.EpisodeRowComponent
import com.artemklymenko.rickipedia.presentation.ui.theme.RickAction
import com.artemklymenko.rickipedia.presentation.ui.theme.RickPrimary
import com.artemklymenko.rickipedia.presentation.ui.theme.RickTextPrimary

@Composable
fun AllEpisodesScreen(
    state: State<AllEpisodesState>,
    modifier: Modifier,
    onEvent: (AllEpisodesEvent) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onEvent(AllEpisodesEvent.GetAllEpisodes)
    }

    state.value.let { stateValue ->
        if(stateValue.isLoading) {
            LoadingState()
        } else if(stateValue.error != null) {
            ErrorMessageComponent(
                message = stateValue.error,
                modifier = modifier
            )
        } else {
            AllEpisodesScreenContent(
                episodes = stateValue.episodes,
                modifier = modifier
            )
        }
    }
}

@Composable
fun AllEpisodesScreenContent(episodes: Map<String, List<DomainEpisode>>, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            episodes.forEach { mapEntry ->
                val uniqueCharacterCount =
                    mapEntry.value.flatMap { it.characterIdsInEpisode }.toSet().size
                item(key = mapEntry.key) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RickPrimary)
                    ) {
                        Text(
                            text = mapEntry.key,
                            color = RickTextPrimary,
                            fontSize = 32.sp
                        )
                        Text(
                            text = "$uniqueCharacterCount unique characters",
                            color = RickTextPrimary,
                            fontSize = 22.sp
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .height(4.dp)
                                .background(
                                    color = RickAction,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }

                }
                mapEntry.value.forEach { episode ->
                    item(key = episode.id) { EpisodeRowComponent(episode = episode) }
                }
            }
        }
    }
}
