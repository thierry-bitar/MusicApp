package com.infomaniak.musicrepository.mapper

import com.infomaniak.musicrepository.model.Song
import com.infomaniak.searchmusic.model.Song as SongFromDomains

fun Song.toSongFromDomains(): SongFromDomains = SongFromDomains(
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
    releaseDate = releaseDate,
    artworkUrl100 = artworkUrl100,
    trackTimeMillis = trackTimeMillis
)