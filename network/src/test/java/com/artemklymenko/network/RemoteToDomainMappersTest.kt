package com.artemklymenko.network

import com.artemklymenko.network.models.data.mappers.toDomainCharacter
import com.artemklymenko.network.models.data.mappers.toDomainCharacterPage
import com.artemklymenko.network.models.data.mappers.toDomainEpisode
import com.artemklymenko.network.models.domain.CharacterGender
import com.artemklymenko.network.models.domain.CharacterStatus
import com.artemklymenko.network.models.remote.RemoteCharacter
import com.artemklymenko.network.models.remote.RemoteCharacterPage
import com.artemklymenko.network.models.remote.RemoteEpisode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RemoteToDomainMappersTest {
    @Test
    fun `test RemoteCharacter to DomainCharacter mapping`(){
        val remoteCharacter = RemoteCharacter(
            created = "2023-01-01T00:00:00.000Z",
            episode = listOf("https://rickandmortyapi.com/api/episode/1", "https://rickandmortyapi.com/api/episode/42"),
            gender = "Male",
            id = 10,
            image = "https://image.url/character.png",
            location = RemoteCharacter.RemoteLocation(name = "Earth", url = "https://location.url/1"),
            name = "Rick Sanchez",
            origin = RemoteCharacter.RemoteOrigin(name = "Earth (C-137)", url = "https://origin.url/1"),
            species = "Human",
            status = "Alive",
            type = "Scientist",
            url = ""
        )

        val domainCharacter = remoteCharacter.toDomainCharacter()

        assertEquals("2023-01-01T00:00:00.000Z", domainCharacter.created)
        assertEquals(listOf(1, 42), domainCharacter.episodeIds)
        assertEquals(CharacterGender.Male, domainCharacter.gender)
        assertEquals(10, domainCharacter.id)
        assertEquals("https://image.url/character.png", domainCharacter.imageUrl)
        assertEquals("Rick Sanchez", domainCharacter.name)
        assertEquals("Human", domainCharacter.species)
        assertEquals("Scientist", domainCharacter.type)
        assertEquals(CharacterStatus.Alive, domainCharacter.status)

        assertEquals("Earth", domainCharacter.location.name)
        assertEquals("https://location.url/1", domainCharacter.location.url)
        assertEquals("Earth (C-137)", domainCharacter.origin.name)
        assertEquals("https://origin.url/1", domainCharacter.origin.url)
    }

    @Test
    fun `test RemoteEpisode to DomainEpisode mapping`() {
        val remoteEpisode = RemoteEpisode(
            id = 5,
            name = "Meeseeks and Destroy",
            episode = "S01E05",
            airDate = "January 20, 2014",
            characters = listOf(
                "https://rickandmortyapi.com/api/character/1",
                "https://rickandmortyapi.com/api/character/42"
            )
        )
        val domainEpisode = remoteEpisode.toDomainEpisode()

        assertEquals(5, domainEpisode.id)
        assertEquals("Meeseeks and Destroy", domainEpisode.name)
        assertEquals(1, domainEpisode.seasonNumber)
        assertEquals(5, domainEpisode.episodeNumber)
        assertEquals("January 20, 2014", domainEpisode.airDate)
        assertEquals(listOf(1, 42), domainEpisode.characterIdsInEpisode)
    }

    @Test
    fun `test RemoteCharacterPage to DomainCharacterPage mapping`() {
        val remoteCharacterPage = RemoteCharacterPage(
            info = RemoteCharacterPage.Info(count = 1, pages = 1, next = null, prev = null),
            results = listOf(
                RemoteCharacter(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "Scientist",
                    gender = "Male",
                    origin = RemoteCharacter.RemoteOrigin("Earth (C-137)", "https://origin.url"),
                    location = RemoteCharacter.RemoteLocation("Earth", "https://location.url"),
                    image = "https://rick.png",
                    episode = listOf("https://rickandmortyapi.com/api/episode/1"),
                    created = "2017-11-04T18:48:46.250Z",
                    url = ""
                )
            )
        )

        val domainPage = remoteCharacterPage.toDomainCharacterPage()


        assertEquals(1, domainPage.info.count)
        assertEquals(1, domainPage.info.pages)
        assertNull(domainPage.info.next)
        assertNull(domainPage.info.prev)

        val character = domainPage.characters[0]
        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
        assertEquals(CharacterGender.Male, character.gender)
        assertEquals(CharacterStatus.Alive, character.status)
        assertEquals(listOf(1), character.episodeIds)
    }
}