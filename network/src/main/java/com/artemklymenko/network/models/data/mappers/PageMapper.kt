package com.artemklymenko.network.models.data.mappers

import com.artemklymenko.network.models.domain.DomainCharacterPage
import com.artemklymenko.network.models.remote.RemoteCharacterPage


fun RemoteCharacterPage.toDomainCharacterPage(): DomainCharacterPage {
    return DomainCharacterPage(
        info = DomainCharacterPage.Info(
            count = info.count,
            pages = info.pages,
            next = info.next,
            prev = info.prev
        ),
        characters = results.map { it.toDomainCharacter() }
    )
}