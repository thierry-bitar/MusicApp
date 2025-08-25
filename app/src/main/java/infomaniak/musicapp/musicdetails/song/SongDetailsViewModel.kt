package infomaniak.musicapp.musicdetails.song

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import infomaniak.musicapp.navigation.songArg
import infomaniak.musicapp.searchmusics.model.Song
import javax.inject.Inject

@HiltViewModel
class SongDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val song: Song = requireNotNull(
        savedStateHandle.get<Song>(songArg)
    )

    init {
        println(song)
    }
}