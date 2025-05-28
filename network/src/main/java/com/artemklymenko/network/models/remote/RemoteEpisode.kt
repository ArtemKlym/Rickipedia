package com.artemklymenko.network.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteEpisode(
    val id: Int,
    val name: String,
    val episode: String,
    @SerialName("air_date")
    val airDate: String,
    val characters: List<String>
)
