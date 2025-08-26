package com.infomaniak.musicrepository.mapper

import com.infomaniak.musicrepository.model.Album
import com.infomaniak.searchmusic.model.Album as AlbumFromDomains

fun Album.toAlbumFromDomains(): AlbumFromDomains = AlbumFromDomains(
    artistId = artistId,
    collectionId = collectionId,
    collectionName = collectionName,
    artistName = artistName,
    artistViewUrl = artistViewUrl,
    collectionViewUrl = collectionViewUrl,
    artworkUrl100 = artworkUrl100,
    trackCount = trackCount,
    primaryGenreName = primaryGenreName,
    releaseDate = releaseDate,
    copyright = copyright,
)