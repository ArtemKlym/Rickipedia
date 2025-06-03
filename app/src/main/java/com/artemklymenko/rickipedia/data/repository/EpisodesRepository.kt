package com.artemklymenko.rickipedia.data.repository

import com.artemklymenko.network.models.core.client.KtorClient
import com.artemklymenko.network.models.core.utils.ApiOperation
import com.artemklymenko.network.models.domain.DomainEpisode
import javax.inject.Inject

class EpisodesRepository @Inject constructor(
    private val ktorClient: KtorClient
) {

    suspend fun fetchAllEpisodes(): ApiOperation<List<DomainEpisode>> = ktorClient.getAllEpisodes()
}