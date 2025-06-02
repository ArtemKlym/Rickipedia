package com.artemklymenko.rickipedia.presentation.character_episodes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.runtime.State
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.network.models.domain.DomainEpisode
import com.artemklymenko.rickipedia.core.components.CharacterImage
import com.artemklymenko.rickipedia.core.components.CharacterNameComponent
import com.artemklymenko.rickipedia.core.components.ErrorMessageComponent
import com.artemklymenko.rickipedia.core.components.LoadingState
import com.artemklymenko.rickipedia.core.components.SimpleToolbar
import com.artemklymenko.rickipedia.presentation.character_details.components.SeasonHeader
import com.artemklymenko.rickipedia.presentation.character_episodes.components.EpisodeRowComponent
import com.artemklymenko.rickipedia.presentation.ui.theme.RickAction

@Composable
fun CharacterEpisodesScreen(
    character: DomainCharacter,
    state: State<CharacterEpisodesState>,
    modifier: Modifier,
    onEvent: (CharacterEpisodesEvent) -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onEvent(CharacterEpisodesEvent.GetCharacterEpisodes(character))
    }

    state.value.let {  stateValue ->
        if(stateValue.isLoading) {
            LoadingState()
        } else if (stateValue.errorMessage != null) {
            ErrorMessageComponent(
                message = stateValue.errorMessage,
                modifier = modifier
            )
        } else {
            CharacterEpisodesScreenContent(
                character = character,
                episodes = stateValue.episodes,
                modifier = modifier,
                onBackClick = onBackClick
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharacterEpisodesScreenContent(
    character: DomainCharacter,
    episodes: List<DomainEpisode>,
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        SimpleToolbar(
            title = "Character episodes",
            onBackAction = onBackClick
        )
        LazyColumn {
            item { CharacterNameComponent(name = character.name) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                LazyRow {
                    episodeBySeasonMap.forEach { mapEntry ->
                        val title = "Season ${mapEntry.key}"
                        item { Spacer(modifier = Modifier.width(16.dp)) }
                        item {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = RickAction,
                                modifier = Modifier.clickable {

                                }
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { CharacterImage(imageUrl = character.imageUrl) }
            item { Spacer(modifier = Modifier.height(8.dp)) }

            episodes.groupBy { it.seasonNumber }.forEach { mapEntry ->
                stickyHeader { SeasonHeader(seasonNumber = mapEntry.key) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(mapEntry.value) { episode ->
                    EpisodeRowComponent(episode)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}