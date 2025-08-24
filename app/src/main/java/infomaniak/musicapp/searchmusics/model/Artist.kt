package infomaniak.musicapp.searchmusics.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val artistId: Long,
    val artistName: String,
    val artistLinkUrl: String? = null,
    val primaryGenreName: String? = null,
) : Parcelable