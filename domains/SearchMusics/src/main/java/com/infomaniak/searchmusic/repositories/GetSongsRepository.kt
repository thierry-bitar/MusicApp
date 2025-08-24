package com.infomaniak.searchmusic.repositories

import com.infomaniak.searchmusic.model.Song

fun interface GetSongsRepository {
    suspend fun searchSongs(term: String, offset: Int): Result<List<Song>>
}