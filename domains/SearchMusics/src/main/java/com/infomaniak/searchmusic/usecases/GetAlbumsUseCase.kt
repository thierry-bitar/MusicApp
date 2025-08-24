package com.infomaniak.searchmusic.usecases

import com.infomaniak.searchmusic.model.Album
import com.infomaniak.searchmusic.repositories.GetAlbumsRepository
import javax.inject.Inject

fun interface GetAlbumsUseCase {
    suspend operator fun invoke(
        term: String,
        offset: Int
    ): Result<List<Album>>
}

class GetAlbumsUseCaseImpl @Inject constructor(
    private val repository: GetAlbumsRepository,
) : GetAlbumsUseCase {
    override suspend operator fun invoke(
        term: String,
        offset: Int
    ): Result<List<Album>> = repository.searchAlbums(
        term = term,
        offset = offset
    )
}