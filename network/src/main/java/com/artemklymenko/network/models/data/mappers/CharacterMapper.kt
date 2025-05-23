package com.artemklymenko.network.models.data.mappers

import com.artemklymenko.network.models.domain.CharacterGender
import com.artemklymenko.network.models.domain.CharacterStatus
import com.artemklymenko.network.models.domain.DomainCharacter
import com.artemklymenko.network.models.remote.RemoteCharacter

fun RemoteCharacter.toDomainCharacter(): DomainCharacter {
    val characterGender = when (gender.lowercase()) {
        "female" -> CharacterGender.Female
        "male" -> CharacterGender.Male
        "genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }
    val characterStatus = when (status.lowercase()) {
        "alive" -> CharacterStatus.Alive
        "dead" -> CharacterStatus.Dead
        else -> CharacterStatus.Unknown
    }
    return DomainCharacter(
        created = created,
        episodeIds = episode.map { it.substring(it.lastIndexOf("/") + 1).toInt() },
        gender = characterGender,
        id = id,
        imageUrl = image,
        location = DomainCharacter.Location(name = location.name, url = location.url),
        name = name,
        origin = DomainCharacter.Origin(name = origin.name, url = origin.url),
        species = species,
        status = characterStatus,
        type = type
    )
}