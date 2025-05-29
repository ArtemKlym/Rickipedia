package com.artemklymenko.rickipedia.data.repository

import com.artemklymenko.network.models.core.client.KtorClient
import com.artemklymenko.network.models.core.utils.ApiOperation
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.network.models.domain.DomainCharacterPage
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val ktorClient: KtorClient
) {
    suspend fun fetchCharactersPage(page: Int): ApiOperation<DomainCharacterPage> {
        return ktorClient.getCharacterByPage(page)
    }

    suspend fun fetchCharacter(characterId: Int): ApiOperation<DomainCharacter> {
        return ktorClient.getCharacter(characterId)
    }
}