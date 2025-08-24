package com.infomaniak.searchmusic.repositories

import com.infomaniak.searchmusic.model.Album

fun interface GetAlbumsRepository {
    suspend fun searchAlbums(term: String, offset: Int): Result<List<Album>>
}