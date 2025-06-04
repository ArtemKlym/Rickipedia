package com.artemklymenko.rickipedia.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.rickipedia.core.components.CharacterListItem
import com.artemklymenko.rickipedia.core.components.ErrorMessageComponent
import com.artemklymenko.rickipedia.core.components.LoadingState
import com.artemklymenko.rickipedia.core.components.SimpleToolbar
import com.artemklymenko.rickipedia.core.domain.DataPoint
import com.artemklymenko.rickipedia.presentation.ui.theme.RickAction
import com.artemklymenko.rickipedia.presentation.ui.theme.RickPrimary
import com.artemklymenko.rickipedia.presentation.ui.theme.RickTextPrimary
import com.artemklymenko.rickipedia.presentation.ui.theme.RickTextSecondary

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        SimpleToolbar(title = "Search")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = uiState.query,
                    onValueChange = { onEvent(SearchUiEvent.OnQueryChanged(it)) },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = RickPrimary
                        )
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = RickPrimary)
                )
            }
            AnimatedVisibility(visible = uiState.query.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear",
                    tint = RickAction,
                    modifier = Modifier.clickable {
                        onEvent(SearchUiEvent.OnQueryChanged(""))
                    }
                )
            }
        }
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { LoadingState() }
                )
            }

            uiState.errorMessage != null -> {
                ErrorMessageComponent(
                    message = uiState.errorMessage,
                    modifier = modifier
                )
            }

            uiState.filterState != null -> {
                Text(
                    text = "${uiState.allResults.size} results for ${uiState.query}",
                    color = RickTextPrimary,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
                SearchScreenContent(
                    characters = uiState.results,
                    filterState = uiState.filterState,
                    allCharacters = uiState.allResults,
                    onEvent = onEvent,
                    onCharacterClick = {}
                )
            }
        }

    }
}

@Composable
fun SearchScreenContent(
    characters: List<DomainCharacter>,
    filterState: SearchUiState.FilterState,
    allCharacters: List<DomainCharacter>,
    onCharacterClick: () -> Unit,
    onEvent: (SearchUiEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        filterState.statuses.forEach { status ->
            val isSelected = filterState.selectedStatus == status
            val contentColor = if (isSelected) RickTextPrimary else RickTextSecondary
            val backgroundColor = if (isSelected) contentColor.copy(alpha = 0.1f) else Color.Transparent
            val count = allCharacters.count { it.status == status }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .border(
                        width = 1.dp,
                        color = contentColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onEvent(SearchUiEvent.OnStatusClick(status)) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = count.toString(),
                    color = contentColor,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = status.displayName,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 8.dp)
    ) {
        items(characters) { character ->
            val dataPoints = buildList {
                add(DataPoint("Last known location", character.location.name))
                add(DataPoint("Species", character.species))
                add(DataPoint("Gender", character.gender.displayName))
                character.type.takeIf { it.isNotEmpty() }?.let { type ->
                    add(DataPoint("Type", type))
                }
                add(DataPoint("Origin", character.origin.name))
                add(DataPoint("Episode count", character.episodeIds.size.toString()))
            }
            CharacterListItem(
                character = character,
                characterDataPoints = dataPoints,
                onClick = {
                    onCharacterClick.invoke()
                }
            )
        }
    }
    Spacer(
        modifier = Modifier
            .height(8.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(colors = listOf(RickPrimary, Color.Transparent))
            )
    )
}
