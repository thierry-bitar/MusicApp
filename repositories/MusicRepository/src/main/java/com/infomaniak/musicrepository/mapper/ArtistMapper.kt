package com.infomaniak.musicrepository.mapper

import com.infomaniak.musicrepository.model.Artist
import com.infomaniak.searchmusic.model.Artist as ArtistFromDomains

fun Artist.toArtistFromDomains(): ArtistFromDomains = ArtistFromDomains(
    artistId = artistId,
    artistName = artistName,
    artistLinkUrl = artistLinkUrl,
    primaryGenreName = primaryGenreName
)