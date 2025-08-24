package com.infomaniak.musicrepository

import com.infomaniak.musicrepository.model.SearchAlbumsResponse
import com.infomaniak.musicrepository.model.SearchArtistsResponse
import com.infomaniak.musicrepository.model.SearchSongsResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

internal class FakeMusicApiService(
    private val onSearchSongs: () -> SearchSongsResponse,
    private val onSearchArtists: () -> SearchArtistsResponse,
    private val onSearchAlbums: () -> SearchAlbumsResponse,
) : MusicApiService {

    override suspend fun searchSongs(
        term: String, media: String, entity: String?, limit: Int, offset: Int
    ): SearchSongsResponse = onSearchSongs()

    override suspend fun searchArtists(
        term: String, media: String, entity: String, limit: Int, offset: Int
    ): SearchArtistsResponse = onSearchArtists()

    override suspend fun searchAlbums(
        term: String, media: String, entity: String, limit: Int, offset: Int
    ): SearchAlbumsResponse = onSearchAlbums()
}

internal fun http404(): HttpException = HttpException(
    Response.error<Any>(
        404,
        """{"error":"not found"}""".toResponseBody("application/json".toMediaType())
    )
)

internal fun http500(): HttpException = HttpException(
    Response.error<Any>(
        500,
        """{"error":"boom"}""".toResponseBody("application/json".toMediaType())
    )
)