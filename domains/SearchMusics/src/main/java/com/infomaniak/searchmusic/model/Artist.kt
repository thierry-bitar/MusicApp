package com.infomaniak.searchmusic.model

data class Artist(
    val artistId: Long,
    val artistName: String,
    val artistLinkUrl: String? = null,
    val primaryGenreName: String? = null,
)