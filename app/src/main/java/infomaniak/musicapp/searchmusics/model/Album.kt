package infomaniak.musicapp.searchmusics.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val artistId: Long,
    val collectionId: Long,

    val collectionName: String,
    val artistName: String,
    val copyright: String? = null,

    val artistViewUrl: String? = null,
    val collectionViewUrl: String? = null,

    val artworkUrl100: String? = null,
    val trackCount: Int? = null,
    val primaryGenreName: String? = null,
    val releaseDate: String? = null,
) : Parcelable