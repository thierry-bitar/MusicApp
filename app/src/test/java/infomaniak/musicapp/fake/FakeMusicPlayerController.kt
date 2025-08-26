package infomaniak.musicapp.fake

import infomaniak.musicapp.musicdetails.MusicPlayerController
import infomaniak.musicapp.musicdetails.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class FakeMusicPlayerController : MusicPlayerController {

    private val _state = MutableStateFlow(PlayerState())
    override val state: StateFlow<PlayerState> = _state

    var lastLoadedUrl: String? = null
    var playCalls = 0
    var pauseCalls = 0
    var lastSeekMs: Long? = null
    var released = false

    override fun load(url: String) {
        lastLoadedUrl = url
        _state.value = _state.value.copy(
            isLoading = true,
            isError = false,
            isEnded = false,
            isPlaying = false,
            positionMs = 0L
        )
    }

    override fun play() {
        playCalls++
        _state.value = _state.value.copy(isPlaying = true, isError = false)
    }

    override fun pause() {
        pauseCalls++
        _state.value = _state.value.copy(isPlaying = false)
    }

    override fun seekTo(ms: Long) {
        val clamped = ms.coerceAtLeast(0L)
        lastSeekMs = clamped
        _state.value = _state.value.copy(positionMs = clamped)
    }

    override fun release() {
        released = true
    }

    fun emitReady(durationMs: Long) {
        _state.value = _state.value.copy(
            isLoading = false,
            isError = false,
            durationMs = durationMs
        )
    }

    fun emitProgress(positionMs: Long) {
        _state.value = _state.value.copy(positionMs = positionMs)
    }

    fun emitEnded() {
        _state.value = _state.value.copy(isEnded = true, isPlaying = false)
    }

    fun emitError() {
        _state.value = _state.value.copy(isError = true, isLoading = false, isPlaying = false)
    }
}