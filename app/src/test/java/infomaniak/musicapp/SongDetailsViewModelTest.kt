package infomaniak.musicapp

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.infomaniak.searchmusic.model.Song
import com.infomaniak.searchmusic.randomSong
import infomaniak.musicapp.fake.FakeMusicPlayerController
import infomaniak.musicapp.musicdetails.SongDetailsViewModel
import infomaniak.musicapp.navigation.songArg
import infomaniak.musicapp.searchmusics.mapper.toSong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SongDetailsViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Check SongDetailsUiState values at init`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem().playerState.apply {
                    assertFalse(isLoading)
                    assertFalse(isPlaying)
                    assertEquals(0L, durationMs)
                    assertEquals(0L, positionMs)
                }

                awaitItem().apply {
                    assertTrue(playerState.isLoading)
                    assertFalse(playerState.isPlaying)
                    assertFalse(playerState.error)
                    assertEquals(song.previewUrl, fakePlayer.lastLoadedUrl)
                }

                fakePlayer.emitReady(durationMs = 90_000)
                awaitItem().apply {
                    assertFalse(playerState.isLoading)
                    assertEquals(90_000, playerState.durationMs)
                }

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should play then pause update uiState and call controller`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem() // init idle
                awaitItem() // load triggered

                fakePlayer.emitReady(durationMs = 120_000)
                awaitItem() // ready

                // Play
                viewModel.play()
                awaitItem().apply {
                    assertTrue(playerState.isPlaying)
                }
                assertEquals(1, fakePlayer.playCalls)

                viewModel.pause()
                awaitItem().apply {
                    assertFalse(playerState.isPlaying)
                }
                assertEquals(1, fakePlayer.pauseCalls)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should seekTo update position and delegate to controller`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem() // idle
                awaitItem() // loading after load

                fakePlayer.emitReady(durationMs = 90_000)
                awaitItem() // ready

                viewModel.seekTo(30_000)
                awaitItem().apply {
                    assertEquals(30_000, playerState.positionMs)
                }
                assertEquals(30_000, fakePlayer.lastSeekMs)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should clamp negative seek to 0 and delegate 0 to controller`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem() // idle
                awaitItem() // loading
                fakePlayer.emitReady(durationMs = 90_000)
                awaitItem() // ready

                fakePlayer.emitProgress(1_200)
                awaitItem().apply {
                    assertEquals(1_200, playerState.positionMs)
                }

                viewModel.seekTo(-10)
                awaitItem().apply {
                    assertEquals(0L, playerState.positionMs)
                }
                assertEquals(0L, fakePlayer.lastSeekMs)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should surface error then clear it on retry`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem() // idle
                awaitItem() // loading

                fakePlayer.emitError()
                awaitItem().apply {
                    assertTrue(playerState.error)
                    assertFalse(playerState.isLoading)
                    assertFalse(playerState.isPlaying)
                }

                viewModel.retry()
                awaitItem().apply {
                    assertTrue(playerState.isLoading)
                    assertFalse(playerState.error)
                }
                fakePlayer.emitReady(durationMs = 42_000)
                awaitItem().apply {
                    assertFalse(playerState.isLoading)
                    assertEquals(42_000, playerState.durationMs)
                }

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should NOT load at init when previewUrl is null`() = runTest {
        val songWithoutPreview = randomSong().toSong().copy(previewUrl = null)
        val savedStateHandle = SavedStateHandle(mapOf(songArg to songWithoutPreview))
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(
            savedStateHandle = savedStateHandle,
            fakePlayer = fakePlayer
        )

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem().apply {
                    assertFalse(playerState.isLoading)
                    assertFalse(playerState.isPlaying)
                }
                assertEquals(null, fakePlayer.lastLoadedUrl)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should mark endReached when controller ends`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem() // idle
                awaitItem() // loading
                fakePlayer.emitReady(durationMs = 10_000)
                awaitItem() // ready

                fakePlayer.emitEnded()
                awaitItem().apply {
                    assertTrue(playerState.isEndReached)
                    assertFalse(playerState.isPlaying)
                }

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should release controller when ViewModel is cleared`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        val m = SongDetailsViewModel::class.java.getDeclaredMethod("onCleared")
        m.isAccessible = true
        m.invoke(viewModel)

        assertTrue(fakePlayer.released)
    }

    @Test
    fun `Should be idempotent on repeated play and pause`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem() // idle
                awaitItem() // loading

                fakePlayer.emitReady(durationMs = 60_000)
                awaitItem() // ready

                viewModel.play()
                val play1 = awaitItem()
                assertTrue(play1.playerState.isPlaying)
                assertEquals(1, fakePlayer.playCalls)

                viewModel.play()
                assertEquals(2, fakePlayer.playCalls)
                expectNoEvents()

                viewModel.pause()
                val pause1 = awaitItem()
                assertFalse(pause1.playerState.isPlaying)
                assertEquals(1, fakePlayer.pauseCalls)

                viewModel.pause()
                assertEquals(2, fakePlayer.pauseCalls)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should update position on progress tick without seek`() = runTest {
        val fakePlayer = FakeMusicPlayerController()
        val viewModel = initViewModel(fakePlayer = fakePlayer)

        turbineScope {
            viewModel.songDetailsUiState.test {
                awaitItem() // idle
                awaitItem() // loading

                fakePlayer.emitReady(durationMs = 100_000)
                awaitItem() // ready

                fakePlayer.emitProgress(1_000)
                awaitItem().apply { assertEquals(1_000, playerState.positionMs) }

                fakePlayer.emitProgress(2_000)
                awaitItem().apply { assertEquals(2_000, playerState.positionMs) }

                assertFalse(viewModel.songDetailsUiState.value.playerState.isPlaying)

                expectNoEvents()
            }
        }
    }

    private fun initViewModel(
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
        fakePlayer: FakeMusicPlayerController = FakeMusicPlayerController()
    ): SongDetailsViewModel {
        if (savedStateHandle.get<Song>(songArg) == null) {
            savedStateHandle[songArg] = randomSong().toSong()
        }

        return SongDetailsViewModel(
            savedStateHandle = savedStateHandle,
            player = fakePlayer,
        )
    }
}