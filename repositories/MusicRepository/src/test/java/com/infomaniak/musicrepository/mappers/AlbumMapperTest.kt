package com.infomaniak.musicrepository.mappers

import com.infomaniak.musicrepository.mapper.toAlbumFromDomains
import com.infomaniak.musicrepository.randomAlbum
import com.infomaniak.searchmusic.model.Album
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class AlbumMapperTest {

    @Test
    fun `test toAlbum mapper to map all fields from domain to ui model`() {
        val album = randomAlbum()

        val albumFromDomains: Album = album.toAlbumFromDomains()

        assertEquals(album.artistId, albumFromDomains.artistId)
        assertEquals(album.collectionId, albumFromDomains.collectionId)
        assertEquals(album.collectionName, albumFromDomains.collectionName)
        assertEquals(album.artistName, albumFromDomains.artistName)
        assertEquals(album.copyright, albumFromDomains.copyright)
        assertEquals(album.artistViewUrl, albumFromDomains.artistViewUrl)
        assertEquals(album.collectionViewUrl, albumFromDomains.collectionViewUrl)
        assertEquals(album.artworkUrl100, albumFromDomains.artworkUrl100)
        assertEquals(album.trackCount, albumFromDomains.trackCount)
        assertEquals(album.primaryGenreName, albumFromDomains.primaryGenreName)
        assertEquals(runCatching {
            album.releaseDate?.let {
                Instant.parse(it)
            }
        }.getOrNull(), albumFromDomains.releaseInstant)
    }
}