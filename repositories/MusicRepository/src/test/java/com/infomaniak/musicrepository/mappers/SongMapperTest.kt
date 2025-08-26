package com.infomaniak.musicrepository.mappers

import com.infomaniak.musicrepository.mapper.toSongFromDomains
import com.infomaniak.musicrepository.randomSong
import com.infomaniak.searchmusic.model.Song
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class SongMapperTest {

    @Test
    fun `test toSong mapper to map all fields from domain to ui model`() {
        val song = randomSong()

        val songFromDomains: Song = song.toSongFromDomains()

        assertEquals(song.trackId, songFromDomains.trackId)
        assertEquals(song.artistId, songFromDomains.artistId)
        assertEquals(song.collectionId, songFromDomains.collectionId)
        assertEquals(song.artistName, songFromDomains.artistName)
        assertEquals(song.collectionName, songFromDomains.collectionName)
        assertEquals(song.trackName, songFromDomains.trackName)
        assertEquals(song.trackViewUrl, songFromDomains.trackViewUrl)
        assertEquals(song.previewUrl, songFromDomains.previewUrl)
        assertEquals(song.artistViewUrl, songFromDomains.artistViewUrl)
        assertEquals(song.collectionViewUrl, songFromDomains.collectionViewUrl)
        assertEquals(song.collectionArtistViewUrl, songFromDomains.collectionArtistViewUrl)
        assertEquals(song.primaryGenreName, songFromDomains.primaryGenreName)
        assertEquals(runCatching {
            song.releaseDate?.let {
                Instant.parse(it)
            }
        }.getOrNull(), songFromDomains.releaseInstant)
        assertEquals(song.artworkUrl100, songFromDomains.artworkUrl100)
        assertEquals(song.trackTimeMillis, songFromDomains.trackTimeMillis)
    }
}