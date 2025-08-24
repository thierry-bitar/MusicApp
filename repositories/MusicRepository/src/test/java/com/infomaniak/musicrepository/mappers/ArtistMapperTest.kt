package com.infomaniak.musicrepository.mappers

import com.infomaniak.musicrepository.mapper.toArtistFromDomains
import com.infomaniak.musicrepository.randomArtist
import com.infomaniak.searchmusic.model.Artist
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistMapperTest {

    @Test
    fun `test toArtist mapper to map all fields from domain to ui model`() {
        val artist = randomArtist()

        val artistFromDomains: Artist = artist.toArtistFromDomains()

        assertEquals(artist.artistId, artistFromDomains.artistId)
        assertEquals(artist.artistName, artistFromDomains.artistName)
        assertEquals(artist.artistLinkUrl, artistFromDomains.artistLinkUrl)
        assertEquals(artist.primaryGenreName, artistFromDomains.primaryGenreName)
    }
}