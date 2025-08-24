package com.infomaniak.searchmusic.repositories

import com.infomaniak.searchmusic.model.Artist

fun interface GetArtistsRepository {
    suspend fun searchArtists(term: String, offset: Int): Result<List<Artist>>
}