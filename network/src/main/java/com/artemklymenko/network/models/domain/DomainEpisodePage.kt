package com.artemklymenko.network.models.domain

data class DomainEpisodePage(
    val info: Info,
    val episodes: List<DomainEpisode>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}
