package com.infomaniak.searchmusic.usecases

import com.infomaniak.searchmusic.model.Song
import com.infomaniak.searchmusic.repositories.GetSongsRepository
import javax.inject.Inject

fun interface GetSongsUseCase {
    suspend operator fun invoke(
        term: String,
        offset: Int
    ): Result<List<Song>>
}

class GetSongsUseCaseImpl @Inject constructor(
    private val repository: GetSongsRepository,
) : GetSongsUseCase {
    override suspend operator fun invoke(
        term: String,
        offset: Int
    ): Result<List<Song>> = repository.searchSongs(
        term = term,
        offset = offset
    )
}