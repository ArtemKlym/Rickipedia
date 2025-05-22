package com.artemklymenko.network.models.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: RemoteOrigin,
    val location: RemoteLocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
) {
    @Serializable
    data class RemoteLocation(
        val name: String,
        val url: String
    )

    @Serializable
    data class RemoteOrigin(
        val name: String,
        val url: String
    )
}
