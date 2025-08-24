package infomaniak.musicapp.mappers

import infomaniak.musicapp.randomAlbum
import infomaniak.musicapp.searchmusics.mapper.toAlbum
import infomaniak.musicapp.searchmusics.model.Album
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumMapperTest {

    @Test
    fun `test toAlbum mapper to map all fields from domain to ui model`() {
        val albumFromDomains = randomAlbum()

        val album: Album = albumFromDomains.toAlbum()

        assertEquals(albumFromDomains.artistId, album.artistId)
        assertEquals(albumFromDomains.collectionId, album.collectionId)
        assertEquals(albumFromDomains.collectionName, album.collectionName)
        assertEquals(albumFromDomains.artistName, album.artistName)
        assertEquals(albumFromDomains.artistViewUrl, album.artistViewUrl)
        assertEquals(albumFromDomains.collectionViewUrl, album.collectionViewUrl)
        assertEquals(albumFromDomains.artworkUrl100, album.artworkUrl100)
        assertEquals(albumFromDomains.trackCount, album.trackCount)
        assertEquals(albumFromDomains.primaryGenreName, album.primaryGenreName)
        assertEquals(albumFromDomains.releaseDate, album.releaseDate)
    }
}