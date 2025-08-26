package infomaniak.musicapp.mappers

import com.infomaniak.searchmusic.randomSong
import infomaniak.musicapp.searchmusics.mapper.toSong
import infomaniak.musicapp.searchmusics.model.Song
import org.junit.Assert.assertEquals
import org.junit.Test

class SongMapperTest {

    @Test
    fun `test toSong mapper to map all fields from domain to ui model`() {
        val songFromDomains = randomSong()

        val song: Song = songFromDomains.toSong()

        assertEquals(songFromDomains.trackId, song.trackId)
        assertEquals(songFromDomains.artistId, song.artistId)
        assertEquals(songFromDomains.collectionId, song.collectionId)
        assertEquals(songFromDomains.artistName, song.artistName)
        assertEquals(songFromDomains.collectionName, song.collectionName)
        assertEquals(songFromDomains.trackName, song.trackName)
        assertEquals(songFromDomains.trackViewUrl, song.trackViewUrl)
        assertEquals(songFromDomains.previewUrl, song.previewUrl)
        assertEquals(songFromDomains.artistViewUrl, song.artistViewUrl)
        assertEquals(songFromDomains.collectionViewUrl, song.collectionViewUrl)
        assertEquals(songFromDomains.collectionArtistViewUrl, song.collectionArtistViewUrl)
        assertEquals(songFromDomains.primaryGenreName, song.primaryGenreName)
        assertEquals(songFromDomains.releaseInstant, song.releaseInstant)
        assertEquals(songFromDomains.artworkUrl100, song.artworkUrl100)
        assertEquals(songFromDomains.trackTimeMillis, song.trackTimeMillis)
    }
}