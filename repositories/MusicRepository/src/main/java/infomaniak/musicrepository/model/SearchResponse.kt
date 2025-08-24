@file:OptIn(InternalSerializationApi::class)

package infomaniak.musicrepository.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class SearchSongsResponse(
    val resultCount: Int,
    val results: List<Song>
)

@Serializable
data class SearchAlbumsResponse(
    val resultCount: Int,
    val results: List<Album>
)

@Serializable
data class SearchArtistsResponse(
    val resultCount: Int,
    val results: List<Artist>
)

@Serializable
data class Song(
    val trackId: Long,
    val artistId: Long,
    val collectionId: Long,

    val artistName: String,
    val collectionName: String,
    val trackName: String,

    val trackViewUrl: String? = null,
    val previewUrl: String? = null,
    val artistViewUrl: String? = null,
    val collectionViewUrl: String? = null,
    val collectionArtistViewUrl: String? = null,

    val primaryGenreName: String? = null,
    val releaseDate: String? = null,
    val artworkUrl100: String? = null,
    val trackTimeMillis: Int? = null,
)

@Serializable
data class Album(
    val artistId: Long,
    val collectionId: Long,

    val collectionName: String,
    val artistName: String,

    val artistViewUrl: String? = null,
    val collectionViewUrl: String? = null,

    val artworkUrl100: String? = null,
    val trackCount: Int? = null,
    val primaryGenreName: String? = null,
    val releaseDate: String? = null,
)

@Serializable
data class Artist(
    val artistId: Long,
    val artistName: String,
    val artistLinkUrl: String? = null,
    val primaryGenreName: String? = null,
)