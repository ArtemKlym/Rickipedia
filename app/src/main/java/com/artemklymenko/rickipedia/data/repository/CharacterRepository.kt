package com.artemklymenko.rickipedia.data.repository

import com.artemklymenko.network.models.core.client.KtorClient
import com.artemklymenko.network.models.core.utils.ApiOperation
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.network.models.domain.DomainCharacterPage
import com.artemklymenko.network.models.domain.DomainEpisode
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val ktorClient: KtorClient
) {
    suspend fun fetchCharactersPage(
        page: Int,
        params: Map<String, String> = emptyMap()
    ): ApiOperation<DomainCharacterPage> {
        return ktorClient.getCharacterByPage(pageNumber = page, queryParams = params)
    }

    suspend fun fetchCharacter(characterId: Int): ApiOperation<DomainCharacter> {
        return ktorClient.getCharacter(characterId)
    }

    suspend fun fetchCharacterEpisodes(episodeIds: List<Int>): ApiOperation<List<DomainEpisode>> {
        return ktorClient.getEpisodes(episodeIds)
    }

    suspend fun fetchAllCharactersByName(searchQuery: String): ApiOperation<List<DomainCharacter>> {
        return ktorClient.searchAllCharactersByName(searchQuery)
    }
}