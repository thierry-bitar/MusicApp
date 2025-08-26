package infomaniak.musicapp.musicdetails

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infomaniak.musicapp.navigation.albumArg
import infomaniak.musicapp.searchmusics.model.Album
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val album: Album = requireNotNull(
        savedStateHandle.get<Album>(albumArg)
    )

    val albumDetailsUiState: StateFlow<AlbumDetailsUiState> = savedStateHandle.getStateFlow(
        key = ALBUM_DETAILS,
        initialValue = AlbumDetailsUiState(
            album = album,
        ),
    )

    @Parcelize
    data class AlbumDetailsUiState(
        val album: Album,
    ) : Parcelable
}

private const val ALBUM_DETAILS = "handle:album_details"