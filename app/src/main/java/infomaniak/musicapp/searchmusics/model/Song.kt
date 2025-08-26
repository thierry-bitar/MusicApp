package infomaniak.musicapp.searchmusics.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.time.ZoneId.systemDefault

@Parcelize
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
    val releaseInstant: Instant? = null,
    val artworkUrl100: String? = null,
    val trackTimeMillis: Int? = null,
) : Parcelable {

    @IgnoredOnParcel
    val releaseYear = releaseInstant?.atZone(systemDefault())
        ?.year
        ?.toString()
}