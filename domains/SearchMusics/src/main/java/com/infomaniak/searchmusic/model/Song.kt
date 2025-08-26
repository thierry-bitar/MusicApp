package com.infomaniak.searchmusic.model

import java.time.Instant

data class Song(
    val trackId: Long,
    val artistId: Long,
    val collectionId: Long,

    val artistName: String,
    val collectionName: String,
    val trackName: String,

    val trackViewUrl: String? = null,
    val previewUrl: String? = null,
    val artistViewUrl: String? = null,
    val collectionViewUrl: String? = null,
    val collectionArtistViewUrl: String? = null,

    val primaryGenreName: String? = null,
    val releaseInstant: Instant? = null,
    val artworkUrl100: String? = null,
    val trackTimeMillis: Int? = null,
)
