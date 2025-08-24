package com.infomaniak.searchmusic.getartistsusecase

import com.infomaniak.searchmusic.model.Artist
import com.infomaniak.searchmusic.repositories.GetArtistsRepository

open class FakeArtistsRepository : GetArtistsRepository {

    override suspend fun searchArtists(
        term: String,
        offset: Int
    ): Result<List<Artist>> {
        TODO("Not yet implemented")
    }
}