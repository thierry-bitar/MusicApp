package com.infomaniak.musicrepository

import android.util.Log
import com.infomaniak.musicrepository.mapper.toAlbumFromDomains
import com.infomaniak.musicrepository.mapper.toArtistFromDomains
import com.infomaniak.musicrepository.mapper.toSongFromDomains
import com.infomaniak.searchmusic.model.Album
import com.infomaniak.searchmusic.model.Artist
import com.infomaniak.searchmusic.model.Song
import com.infomaniak.searchmusic.repositories.GetAlbumsRepository
import com.infomaniak.searchmusic.repositories.GetArtistsRepository
import com.infomaniak.searchmusic.repositories.GetSongsRepository
import retrofit2.HttpException
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val musicApiService: MusicApiService,
) : GetSongsRepository, GetAlbumsRepository, GetArtistsRepository {

    override suspend fun searchSongs(
        term: String,
        offset: Int
    ): Result<List<Song>> = apiCall {
        musicApiService.searchSongs(
            term = term,
            offset = offset
        )
    }.mapResults { response -> response.results.map { songDto -> songDto.toSongFromDomains() } }

    override suspend fun searchArtists(
        term: String,
        offset: Int
    ): Result<List<Artist>> = apiCall {
        musicApiService.searchArtists(
            term = term,
            offset = offset
        )
    }.mapResults { response -> response.results.map { artistDto -> artistDto.toArtistFromDomains() } }

    override suspend fun searchAlbums(
        term: String,
        offset: Int
    ): Result<List<Album>> = apiCall {
        musicApiService.searchAlbums(
            term = term,
            offset = offset
        )
    }.mapResults { response -> response.results.map { albumDto -> albumDto.toAlbumFromDomains() } }
}

private inline fun <T> apiCall(block: () -> T): Result<T> =
    runCatching { block() }
        .onFailure { throwable ->
            Log.e("MusicRepositoryImpl", "API call failed", throwable)
        }

private inline fun <T, R> Result<T>.mapResults(
    crossinline toList: (T) -> List<R>
): Result<List<R>> =
    this.mapCatching { toList(it) }
        .recoverCatching { t ->
            if (t is HttpException && t.code() == 404) emptyList()
            else throw t
        }
