package infomaniak.musicrepository

import infomaniak.musicrepository.model.SearchAlbumsResponse
import infomaniak.musicrepository.model.SearchArtistsResponse
import infomaniak.musicrepository.model.SearchSongsResponse
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
        // @Query("attribute") attribute: String = "songTerm" // (optionnel, plus tard)
    ): SearchSongsResponse

    @GET("search")
    suspend fun searchArtists(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "musicArtist",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        // @Query("attribute") attribute: String = "artistTerm"
    ): SearchArtistsResponse

    @GET("search")
    suspend fun searchAlbums(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "album",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        // @Query("attribute") attribute: String = "albumTerm"
    ): SearchAlbumsResponse
}