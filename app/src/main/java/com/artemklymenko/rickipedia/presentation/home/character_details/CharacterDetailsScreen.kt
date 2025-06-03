package com.artemklymenko.rickipedia.presentation.home.character_details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.rickipedia.core.components.DataPointComponent
import com.artemklymenko.rickipedia.core.components.ErrorMessageComponent
import com.artemklymenko.rickipedia.core.components.LoadingState
import com.artemklymenko.rickipedia.core.components.SimpleToolbar
import com.artemklymenko.rickipedia.presentation.home.character_details.components.CharacterDetailsNamePlateComponent
import com.artemklymenko.rickipedia.presentation.ui.theme.RickAction
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    modifier: Modifier,
    stateFlow: StateFlow<CharacterDetailsState>,
    onEpisodeClick: (DomainCharacter) -> Unit,
    onEvent: (CharacterDetailsEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val state by stateFlow.collectAsState()

    LaunchedEffect(key1 = Unit) {
        onEvent(CharacterDetailsEvent.GetCharacterDetails(characterId))
    }

    if (state.isLoading) {
        LoadingState()
    } else if (state.errorMessage != null) {
        ErrorMessageComponent(
            message = state.errorMessage,
            modifier = modifier
        )
    } else {
        CharacterDetailsContent(modifier, onBackClick, state, onEpisodeClick)
    }
}

@Composable
private fun CharacterDetailsContent(
    modifier: Modifier,
    onBackClick: () -> Unit,
    state: CharacterDetailsState,
    onEpisodeClick: (DomainCharacter) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        SimpleToolbar(
            title = "Character details",
            onBackAction = onBackClick
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            state.character?.let { character ->
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    CharacterDetailsNamePlateComponent(
                        name = character.name,
                        status = character.status
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    SubcomposeAsyncImage(
                        model = character.imageUrl,
                        contentDescription = "Character image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        loading = {
                            LoadingState()
                        }
                    )
                }

                items(state.dataPoints) {
                    Spacer(modifier = Modifier.height(32.dp))
                    DataPointComponent(dataPoint = it)
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                item {
                    Text(
                        text = "View all episodes",
                        color = RickAction,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .border(
                                width = 1.dp,
                                color = RickAction,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onEpisodeClick(character)
                            }
                            .padding(vertical = 8.dp)
                    )
                }

                item { Spacer(modifier = Modifier.height(64.dp)) }
            }
        }
    }
}

