package infomaniak.musicapp.mappers

import com.infomaniak.searchmusic.randomArtist
import infomaniak.musicapp.searchmusics.mapper.toArtist
import infomaniak.musicapp.searchmusics.model.Artist
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistMapperTest {

    @Test
    fun `test toArtist mapper to map all fields from domain to ui model`() {
        val artistFromDomains = randomArtist()

        val artist: Artist = artistFromDomains.toArtist()

        assertEquals(artistFromDomains.artistId, artist.artistId)
        assertEquals(artistFromDomains.artistName, artist.artistName)
        assertEquals(artistFromDomains.artistLinkUrl, artist.artistLinkUrl)
        assertEquals(artistFromDomains.primaryGenreName, artist.primaryGenreName)
    }
}