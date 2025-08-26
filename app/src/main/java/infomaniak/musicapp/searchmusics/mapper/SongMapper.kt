package infomaniak.musicapp.searchmusics.mapper

import infomaniak.musicapp.searchmusics.model.Song
import com.infomaniak.searchmusic.model.Song as SongFromDomains

internal fun SongFromDomains.toSong(): Song = Song(
    trackId = trackId,
    artistId = artistId,
    collectionId = collectionId,
    artistName = artistName,
    collectionName = collectionName,
    trackName = trackName,
    trackViewUrl = trackViewUrl,
    previewUrl = previewUrl,
    artistViewUrl = artistViewUrl,
    collectionViewUrl = collectionViewUrl,
    collectionArtistViewUrl = collectionArtistViewUrl,
    primaryGenreName = primaryGenreName,
    releaseInstant = releaseInstant,
    artworkUrl100 = artworkUrl100,
    trackTimeMillis = trackTimeMillis
)