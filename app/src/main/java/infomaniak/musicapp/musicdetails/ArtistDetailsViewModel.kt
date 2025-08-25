package infomaniak.musicapp.musicdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infomaniak.musicapp.navigation.artistArg
import infomaniak.musicapp.searchmusics.model.Artist
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val artist: Artist = requireNotNull(
        savedStateHandle.get<Artist>(artistArg)
    )
}