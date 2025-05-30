package com.artemklymenko.rickipedia.presentation.characters_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemklymenko.rickipedia.core.components.ErrorMessageComponent
import com.artemklymenko.rickipedia.core.components.LoadingState
import com.artemklymenko.rickipedia.core.components.SimpleToolbar
import com.artemklymenko.rickipedia.presentation.characters_list.components.CharacterGridItem

@Composable
fun CharactersListScreen(
    modifier: Modifier = Modifier,
    state: State<CharactersListState>,
    onCharacterClick: (Int) -> Unit,
    onEvent: (CharactersListEvent) -> Unit
) {

    LaunchedEffect(Unit) {
        onEvent(CharactersListEvent.GetInitialPage)
    }

    val scrollState = rememberLazyGridState()

    val fetchNextPage: Boolean by remember {
        derivedStateOf {
            val currentCharacterCount = state.value.characters.size
            val lastDisplayedIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            return@derivedStateOf lastDisplayedIndex >= currentCharacterCount - 10
        }
    }

    LaunchedEffect(key1 = fetchNextPage) {
        if (fetchNextPage) {
            onEvent(CharactersListEvent.GetNextPage)
        }
    }

    state.value.let { stateValue ->
        if (stateValue.isLoading) {
            LoadingState()
        } else if (stateValue.errorMessage != null) {
            ErrorMessageComponent(
                message = stateValue.errorMessage,
                modifier = modifier
            )
        } else {
            CharactersListContent(
                modifier = modifier,
                scrollState = scrollState,
                stateValue = stateValue,
                onCharacterClick = onCharacterClick
            )
        }
    }
}

@Composable
private fun CharactersListContent(
    modifier: Modifier,
    scrollState: LazyGridState,
    stateValue: CharactersListState,
    onCharacterClick: (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SimpleToolbar("All characters")
        LazyVerticalGrid(
            state = scrollState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = stateValue.characters,
                key = { it.id }
            ) { character ->
                CharacterGridItem(modifier = Modifier, character = character) {
                    onCharacterClick(character.id)
                }
            }
        }
    }
}