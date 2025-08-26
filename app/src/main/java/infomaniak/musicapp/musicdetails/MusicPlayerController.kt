package infomaniak.musicapp.musicdetails

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

interface MusicPlayerController {
    val state: StateFlow<PlayerState>
    fun load(url: String)
    fun play()
    fun pause()
    fun seekTo(ms: Long)
    fun release()
}

class MusicPlayerControllerImpl @Inject constructor(
    private val player: ExoPlayer
) : MusicPlayerController {

    private val _state = MutableStateFlow(PlayerState())
    override val state: StateFlow<PlayerState> = _state

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                _state.update {
                    it.copy(
                        isEnded = playbackState == Player.STATE_ENDED,
                        isError = if (playbackState == Player.STATE_READY) false else it.isError
                    )
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlayerError(error: PlaybackException) {
                _state.update {
                    it.copy(
                        isError = true,
                        isLoading = false
                    )
                }
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                _state.update {
                    it.copy(
                        isLoading = isLoading,
                        isError = if (isLoading) false else it.isError
                    )
                }
            }
        })
        scope.launch {
            while (isActive) {
                _state.update {
                    it.copy(
                        durationMs = max(0L, player.duration),
                        positionMs = player.currentPosition
                    )
                }
                delay(75)
            }
        }
    }

    override fun load(url: String) {
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.seekTo(0L)
    }

    override fun play() = player.play()
    override fun pause() = player.pause()
    override fun seekTo(ms: Long) = player.seekTo(ms.coerceAtLeast(0L))

    override fun release() {
        scope.cancel()
        player.release()
    }
}

data class PlayerState(
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isPlaying: Boolean = false,
    val isEnded: Boolean = false,
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)