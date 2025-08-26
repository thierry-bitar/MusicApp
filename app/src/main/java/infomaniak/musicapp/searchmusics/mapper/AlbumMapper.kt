package infomaniak.musicapp.searchmusics.mapper

import infomaniak.musicapp.searchmusics.model.Album
import com.infomaniak.searchmusic.model.Album as AlbumFromDomains

internal fun AlbumFromDomains.toAlbum(): Album = Album(
    artistId = artistId,
    collectionId = collectionId,
    collectionName = collectionName,
    artistName = artistName,
    artistViewUrl = artistViewUrl,
    copyright = copyright,
    collectionViewUrl = collectionViewUrl,
    artworkUrl100 = artworkUrl100,
    trackCount = trackCount,
    primaryGenreName = primaryGenreName,
    releaseDate = releaseDate
)