package com.infomaniak.musicrepository

import com.infomaniak.musicrepository.model.SearchAlbumsResponse
import com.infomaniak.musicrepository.model.SearchArtistsResponse
import com.infomaniak.musicrepository.model.SearchSongsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String? = "song",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("attribute") attribute: String = "songTerm",
        @Query("country") country: String = "FR",
        @Query("lang") lang: String = "fr_fr",
    ): SearchSongsResponse

    @GET("search")
    suspend fun searchArtists(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "musicArtist",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("attribute") attribute: String = "artistTerm",
        @Query("country") country: String = "FR",
        @Query("lang") lang: String = "fr_fr",
    ): SearchArtistsResponse

    @GET("search")
    suspend fun searchAlbums(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "album",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("attribute") attribute: String = "albumTerm",
        @Query("country") country: String = "FR",
        @Query("lang") lang: String = "fr_fr",
    ): SearchAlbumsResponse
}