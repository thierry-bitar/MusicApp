package infomaniak.musicapp.musicdetails

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infomaniak.musicapp.navigation.artistArg
import infomaniak.musicapp.searchmusics.model.Artist
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val artist: Artist = requireNotNull(
        savedStateHandle.get<Artist>(artistArg)
    )

    val artistDetailsUiState: StateFlow<ArtistDetailsUiState> = savedStateHandle.getStateFlow(
        key = ARTIST_DETAILS,
        initialValue = ArtistDetailsUiState(
            artist = artist,
        ),
    )

    @Parcelize
    data class ArtistDetailsUiState(
        val artist: Artist,
    ) : Parcelable
}

private const val ARTIST_DETAILS = "handle:artist_details"