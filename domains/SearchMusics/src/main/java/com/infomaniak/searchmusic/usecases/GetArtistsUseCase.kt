package com.infomaniak.searchmusic.usecases

import com.infomaniak.searchmusic.model.Artist
import com.infomaniak.searchmusic.repositories.GetArtistsRepository
import javax.inject.Inject

fun interface GetArtistsUseCase {
    suspend operator fun invoke(
        term: String,
        offset: Int
    ): Result<List<Artist>>
}

class GetArtistsUseCaseImpl @Inject constructor(
    private val repository: GetArtistsRepository,
) : GetArtistsUseCase {
    override suspend operator fun invoke(
        term: String,
        offset: Int
    ): Result<List<Artist>> = repository.searchArtists(
        term = term,
        offset = offset
    )
}