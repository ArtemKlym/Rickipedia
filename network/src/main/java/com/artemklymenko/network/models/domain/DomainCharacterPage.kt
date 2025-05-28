package com.artemklymenko.network.models.domain


data class DomainCharacterPage(
    val info: Info,
    val characters: List<DomainCharacter>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}
