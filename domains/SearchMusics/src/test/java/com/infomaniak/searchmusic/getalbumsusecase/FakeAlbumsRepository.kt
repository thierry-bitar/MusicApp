package com.infomaniak.searchmusic.getalbumsusecase

import com.infomaniak.searchmusic.model.Album
import com.infomaniak.searchmusic.repositories.GetAlbumsRepository

open class FakeAlbumsRepository : GetAlbumsRepository {

    override suspend fun searchAlbums(
        term: String,
        offset: Int
    ): Result<List<Album>> {
        TODO("Not yet implemented")
    }
}