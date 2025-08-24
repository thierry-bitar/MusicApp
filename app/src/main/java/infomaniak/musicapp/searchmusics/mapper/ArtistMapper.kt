package infomaniak.musicapp.searchmusics.mapper

import infomaniak.musicapp.searchmusics.model.Artist
import com.infomaniak.searchmusic.model.Artist as ArtistFromDomains

fun ArtistFromDomains.toArtist(): Artist = Artist(
    artistId = artistId,
    artistName = artistName,
    artistLinkUrl = artistLinkUrl,
    primaryGenreName = primaryGenreName
)