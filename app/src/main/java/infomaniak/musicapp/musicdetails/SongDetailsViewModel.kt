package infomaniak.musicapp.musicdetails

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import infomaniak.musicapp.navigation.songArg
import infomaniak.musicapp.searchmusics.model.Song
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class SongDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val player: MusicPlayerController,
) : ViewModel(), MusicPlayerController by player {

    private val song: Song = requireNotNull(
        savedStateHandle.get<Song>(songArg)
    )

    val songDetailsUiState: StateFlow<SongDetailsUiState> = savedStateHandle.getStateFlow(
        key = SONG_DETAILS,
        initialValue = SongDetailsUiState(
            song = song,
        ),
    )

    init {
        song.previewUrl?.let { url ->
            player.load(url = url)
        }

        viewModelScope.launch {
            player.state.collect { playerState ->
                savedStateHandle[SONG_DETAILS] = songDetailsUiState.value.copy(
                    playerState = songDetailsUiState.value.playerState.copy(
                        isPlaying = playerState.isPlaying,
                        durationMs = playerState.durationMs,
                        positionMs = playerState.positionMs,
                        isEndReached = playerState.isEnded,
                        error = playerState.isError,
                        isLoading = playerState.isLoading,
                    )
                )
            }
        }
    }

    internal fun retry() {
        song.previewUrl?.let { url ->
            player.load(url = url)
            player.play()
        }
    }

    internal fun replay() {
        player.seekTo(0L);
        player.play()
    }

    override fun onCleared() = player.release()

    @Parcelize
    data class SongDetailsUiState(
        val song: Song,
        val playerState: PlayerState = PlayerState(),
    ) : Parcelable {

        @Parcelize
        data class PlayerState(
            val isPlaying: Boolean = false,
            val durationMs: Long = 0L,
            val positionMs: Long = 0L,
            val isEndReached: Boolean = false,
            val isLoading: Boolean = false,
            val error: Boolean = false,
        ) : Parcelable
    }
}

private const val SONG_DETAILS = "handle:song_details"