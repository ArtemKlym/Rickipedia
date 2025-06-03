package com.artemklymenko.network.models.data.mappers

import com.artemklymenko.network.models.domain.DomainEpisode
import com.artemklymenko.network.models.domain.DomainEpisodePage
import com.artemklymenko.network.models.remote.RemoteEpisode
import com.artemklymenko.network.models.remote.RemoteEpisodePage

fun RemoteEpisode.toDomainEpisode(): DomainEpisode {
    return DomainEpisode(
        id = id,
        name = name,
        seasonNumber = episode.filter { it.isDigit() }.take(2).toInt(),
        episodeNumber = episode.filter { it.isDigit() }.takeLast(2).toInt(),
        airDate = airDate,
        characterIdsInEpisode = characters.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1).toInt()
        }
    )
}

fun RemoteEpisodePage.toDomainEpisodePage(): DomainEpisodePage {
    return DomainEpisodePage(
        info = DomainEpisodePage.Info(
            count = info.count,
            pages = info.pages,
            next = info.next,
            prev = info.prev
        ),
        episodes = results.map { it.toDomainEpisode() }
    )
}