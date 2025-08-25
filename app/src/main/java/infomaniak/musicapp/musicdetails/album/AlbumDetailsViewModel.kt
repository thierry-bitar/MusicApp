package infomaniak.musicapp.musicdetails.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infomaniak.musicapp.navigation.albumArg
import infomaniak.musicapp.searchmusics.model.Album
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val album: Album = requireNotNull(
        savedStateHandle.get<Album>(albumArg)
    )
}