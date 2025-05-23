package com.artemklymenko.network.models.core.client

import com.artemklymenko.network.models.core.utils.ApiOperation
import com.artemklymenko.network.models.data.mappers.toDomainCharacter
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.network.models.remote.RemoteCharacter
import com.artemklymenko.network.models.core.utils.Constants.BASE_URL
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

    suspend fun getCharacter(id: Int): ApiOperation<DomainCharacter> {
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
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

