package com.infomaniak.searchmusic.getsongsusecase

import com.infomaniak.searchmusic.model.Song
import com.infomaniak.searchmusic.repositories.GetSongsRepository

open class FakeSongsRepository : GetSongsRepository {

    override suspend fun searchSongs(
        term: String,
        offset: Int
    ): Result<List<Song>> {
        TODO("Not yet implemented")
    }
}