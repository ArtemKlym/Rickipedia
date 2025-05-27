package com.artemklymenko.network.models.core.client

import com.artemklymenko.network.models.core.utils.ApiOperation
import com.artemklymenko.network.models.data.mappers.toDomainCharacter
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.network.models.remote.RemoteCharacter
import com.artemklymenko.network.models.core.utils.Constants.BASE_URL
import com.artemklymenko.network.models.data.mappers.toDomainEpisode
import com.artemklymenko.network.models.domain.DomainEpisode
import com.artemklymenko.network.models.remote.RemoteEpisode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.lang.Exception

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest {
            url(BASE_URL)
        }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var characterCache = mutableMapOf<Int, DomainCharacter>()
    private var episodeCache = mutableMapOf<Int, DomainEpisode>()
    private var episodesCache = mutableMapOf<List<Int>, List<DomainEpisode>>()

    suspend fun getCharacter(id: Int): ApiOperation<DomainCharacter> {
        characterCache[id]?.let { return ApiOperation.Success(it) }
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
                .also { characterCache[id] = it }
        }
    }

    private suspend fun getEpisode(episodeId: Int): ApiOperation<DomainEpisode> {
        episodeCache[episodeId]?.let { return ApiOperation.Success(it) }
        return safeApiCall {
            client.get("episode/$episodeId")
                .body<RemoteEpisode>()
                .toDomainEpisode()
                .also { episodeCache[episodeId] = it }
        }
    }

    suspend fun getEpisodes(episodesIds: List<Int>): ApiOperation<List<DomainEpisode>> {
        episodesCache[episodesIds]?.let { return ApiOperation.Success(it) }
        return if(episodesIds.size == 1) {
            getEpisode(episodesIds[0]).mapSuccess {
                listOf(it)
            }
        } else {
            val idsCommaSeparated = episodesIds.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$idsCommaSeparated")
                    .body<List<RemoteEpisode>>()
                    .map { it.toDomainEpisode() }
                    .also { episodesCache[episodesIds] = it }
            }
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        }catch (e: Exception) {
            ApiOperation.Failure(exception = e)
        }
    }
}

