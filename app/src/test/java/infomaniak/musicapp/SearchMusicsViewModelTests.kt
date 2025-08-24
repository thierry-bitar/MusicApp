package infomaniak.musicapp

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.infomaniak.searchmusic.randomAlbums
import com.infomaniak.searchmusic.randomArtists
import com.infomaniak.searchmusic.randomSongs
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus.Error
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus.Idle
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus.Loading
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus.Success
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchType
import junit.framework.TestCase.assertTrue
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
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMusicsViewModelTests {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Check SearchMusicsUiState values at init`() = runTest {
        val viewModel = initViewModel()

        turbineScope {
            viewModel.searchMusicsUiState.test {
                awaitItem().apply {
                    assertEquals(expected = String(), actual = userResearch)
                    assertEquals(expected = ResearchType.MUSICS, actual = userResearchType)
                    assertEquals(expected = Idle, actual = researchStatus)
                    assertFalse(actual = isPaging)
                    assertFalse(actual = isEndReached)
                    assertFalse(actual = isPagingError)
                }

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should debounce user input and only call api one`() = runTest {
        val viewModel = initViewModel()

        turbineScope {
            viewModel.searchMusicsUiState.test {
                skipItems(1) // Skip init event

                viewModel.onUserResearchUpdated(input = charlesAznavourInput)
                viewModel.onUserResearchUpdated(input = bobMarleyInput)
                viewModel.onUserResearchUpdated(input = shakiraInput)

                skipItems(3) // Skip emissions of 3 inputs

                awaitItem().apply {
                    assertEquals(expected = Loading, actual = researchStatus)
                    assertEquals(expected = shakiraInput, actual = userResearch)
                }

                assertIs<Success>(value = awaitItem().researchStatus)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should not emit new event when input is empty`() = runTest {
        val viewModel = initViewModel()

        turbineScope {
            viewModel.searchMusicsUiState.test {
                skipItems(1) // Skip init event
                viewModel.onUserResearchUpdated("")

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should reset userResearch when resetUserResearchField is called`() = runTest {
        val viewModel = initViewModel()

        turbineScope {
            viewModel.searchMusicsUiState.test {
                skipItems(1) // Skip init event

                viewModel.onUserResearchUpdated(shakiraInput)
                assertEquals(expected = shakiraInput, actual = awaitItem().userResearch)

                viewModel.resetUserResearchField()
                assertEquals(expected = "", actual = awaitItem().userResearch)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `Should emit Loading and Success with all researchType when api succeed`() = runTest {
        ResearchType.entries.forEach { type ->
            val viewModel = initViewModel()

            turbineScope {
                viewModel.searchMusicsUiState.test {
                    skipItems(1) // Skip init event

                    val previousType = viewModel.searchMusicsUiState.value.userResearchType

                    if (type != previousType) {
                        viewModel.onResearchTypeChanged(researchType = type)
                        awaitItem().apply {
                            assertEquals(expected = type, actual = userResearchType)
                            assertEquals(expected = "", actual = userResearch)
                            assertEquals(Idle, researchStatus)
                        }
                    }

                    viewModel.onUserResearchUpdated(shakiraInput)
                    assertEquals(expected = shakiraInput, actual = awaitItem().userResearch)

                    assertEquals(expected = Loading, actual = awaitItem().researchStatus)

                    assertIs<Success>(value = awaitItem().researchStatus)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun `Should emit Loading and Error with all researchType when api fail`() = runTest {
        ResearchType.entries.forEach { type ->
            val viewModel = initViewModel(isSuccess = false)

            turbineScope {
                viewModel.searchMusicsUiState.test {
                    skipItems(1) // Skip init event

                    val previousType = viewModel.searchMusicsUiState.value.userResearchType

                    if (type != previousType) {
                        viewModel.onResearchTypeChanged(researchType = type)
                        awaitItem().apply {
                            assertEquals(expected = type, actual = userResearchType)
                            assertEquals(expected = "", actual = userResearch)
                            assertEquals(Idle, researchStatus)
                        }
                    }

                    viewModel.onUserResearchUpdated(shakiraInput)
                    assertEquals(expected = shakiraInput, actual = awaitItem().userResearch)

                    assertEquals(expected = Loading, actual = awaitItem().researchStatus)

                    assertIs<Error>(value = awaitItem().researchStatus)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun `Should emit Loading and Error then Success after retry with all researchType`() = runTest {
        ResearchType.entries.forEach { type ->
            var isSuccess = false
            val viewModel = SearchMusicsViewModel(
                savedStateHandle = SavedStateHandle(),
                getSongsUseCase = { _, _ ->
                    if (isSuccess)
                        Result.success(randomSongs())
                    else
                        Result.failure(Throwable())
                },
                getAlbumsUseCase = { _, _ ->
                    if (isSuccess)
                        Result.success(randomAlbums())
                    else
                        Result.failure(Throwable())
                },
                getArtistsUseCase = { _, _ ->
                    if (isSuccess)
                        Result.success(randomArtists())
                    else
                        Result.failure(Throwable())
                }
            )

            turbineScope {
                viewModel.searchMusicsUiState.test {
                    skipItems(1) // Skip init event

                    val previousType = viewModel.searchMusicsUiState.value.userResearchType

                    if (type != previousType) {
                        viewModel.onResearchTypeChanged(researchType = type)
                        awaitItem().apply {
                            assertEquals(expected = type, actual = userResearchType)
                            assertEquals(expected = "", actual = userResearch)
                            assertEquals(Idle, researchStatus)
                        }
                    }

                    viewModel.onUserResearchUpdated(shakiraInput)
                    assertEquals(expected = shakiraInput, actual = awaitItem().userResearch)

                    assertEquals(expected = Loading, actual = awaitItem().researchStatus)

                    assertIs<Error>(value = awaitItem().researchStatus)

                    isSuccess = true
                    viewModel.retry()

                    assertEquals(expected = Loading, actual = awaitItem().researchStatus)
                    assertIs<Success>(value = awaitItem().researchStatus)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun `Should append next page and clear paging flags when loadNextPage succeeds for all researchType`() =
        runTest {
            ResearchType.entries.forEach { type ->
                val songsPage1 = randomSongs(count = 5)
                val songsPage2 = randomSongs(count = 5)

                val albumsPage1 = randomAlbums(count = 5)
                val albumsPage2 = randomAlbums(count = 5)

                val artistsPage1 = randomArtists(count = 5)
                val artistsPage2 = randomArtists(count = 5)

                val viewModel = SearchMusicsViewModel(
                    savedStateHandle = SavedStateHandle(),
                    getSongsUseCase = { _, offset ->
                        if (offset == 0) Result.success(songsPage1) else Result.success(songsPage2)
                    },
                    getAlbumsUseCase = { _, offset ->
                        if (offset == 0) Result.success(albumsPage1) else Result.success(albumsPage2)
                    },
                    getArtistsUseCase = { _, offset ->
                        if (offset == 0) Result.success(artistsPage1) else Result.success(
                            artistsPage2
                        )
                    },
                )

                turbineScope {
                    viewModel.searchMusicsUiState.test {
                        skipItems(1) //  Skip init event

                        val previousType = viewModel.searchMusicsUiState.value.userResearchType

                        if (type != previousType) {
                            viewModel.onResearchTypeChanged(researchType = type)
                            awaitItem().apply {
                                assertEquals(expected = type, actual = userResearchType)
                                assertEquals(expected = "", actual = userResearch)
                                assertEquals(Idle, researchStatus)
                            }
                        }

                        viewModel.onUserResearchUpdated(shakiraInput)
                        assertEquals(shakiraInput, awaitItem().userResearch)
                        assertEquals(Loading, awaitItem().researchStatus)

                        val success1 = awaitItem().researchStatus as Success
                        val size1 = when (type) {
                            ResearchType.MUSICS -> (success1.items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> (success1.items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> (success1.items as Success.SearchItem.Artists).artists.size
                        }
                        assertEquals(songsPage1.size, size1)

                        viewModel.loadNextPage()
                        assertTrue(awaitItem().isPaging)

                        val after = awaitItem()
                        assertFalse(after.isPaging)
                        assertFalse(after.isEndReached)

                        val success2 = after.researchStatus as Success
                        val songs2 = when (type) {
                            ResearchType.MUSICS -> (success2.items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> (success2.items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> (success2.items as Success.SearchItem.Artists).artists.size
                        }

                        assertEquals(size1 + songsPage2.size, songs2)

                        expectNoEvents()
                    }
                }
            }
        }

    @Test
    fun `Should set endReached when next page returns no new items for all researchType`() =
        runTest {
            ResearchType.entries.forEach { type ->
                val songsPage1 = randomSongs(count = 6)
                val albumsPage1 = randomAlbums(count = 6)
                val artistsPage1 = randomArtists(count = 6)

                val viewModel = SearchMusicsViewModel(
                    savedStateHandle = SavedStateHandle(),
                    getSongsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.MUSICS -> if (offset == 0) Result.success(songsPage1) else Result.success(
                                emptyList()
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getAlbumsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ALBUMS -> if (offset == 0) Result.success(albumsPage1) else Result.success(
                                emptyList()
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getArtistsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ARTISTS -> if (offset == 0) Result.success(artistsPage1) else Result.success(
                                emptyList()
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                )

                turbineScope {
                    viewModel.searchMusicsUiState.test {
                        skipItems(1) // Skip init event

                        val previous = viewModel.searchMusicsUiState.value.userResearchType
                        if (type != previous) {
                            viewModel.onResearchTypeChanged(type)
                            awaitItem().apply {
                                assertEquals(expected = type, actual = userResearchType)
                                assertEquals(expected = "", actual = userResearch)
                                assertEquals(Idle, researchStatus)
                            }
                        }

                        viewModel.onUserResearchUpdated(shakiraInput)
                        assertEquals(shakiraInput, awaitItem().userResearch)
                        assertEquals(Loading, awaitItem().researchStatus)

                        val success1 = awaitItem().researchStatus as Success
                        val size1 = when (type) {
                            ResearchType.MUSICS -> (success1.items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> (success1.items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> (success1.items as Success.SearchItem.Artists).artists.size
                        }
                        val expectedSize1 = when (type) {
                            ResearchType.MUSICS -> songsPage1.size
                            ResearchType.ALBUMS -> albumsPage1.size
                            ResearchType.ARTISTS -> artistsPage1.size
                        }
                        assertEquals(expectedSize1, size1)

                        viewModel.loadNextPage()
                        assertTrue(awaitItem().isPaging)

                        val after = awaitItem()
                        assertFalse(after.isPaging)
                        assertTrue(after.isEndReached)

                        val success2 = after.researchStatus as Success
                        val size2 = when (type) {
                            ResearchType.MUSICS -> (success2.items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> (success2.items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> (success2.items as Success.SearchItem.Artists).artists.size
                        }
                        assertEquals(size1, size2)

                        expectNoEvents()
                    }
                }
            }
        }

    @Test
    fun `Should set pagingError and keep list when next page fails for all researchType`() =
        runTest {
            ResearchType.entries.forEach { type ->
                val songsPage1 = randomSongs(count = 5)
                val albumsPage1 = randomAlbums(count = 5)
                val artistsPage1 = randomArtists(count = 5)

                val viewModel = SearchMusicsViewModel(
                    savedStateHandle = SavedStateHandle(),
                    getSongsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.MUSICS -> if (offset == 0) Result.success(songsPage1) else Result.failure(
                                Throwable()
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getAlbumsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ALBUMS -> if (offset == 0) Result.success(albumsPage1) else Result.failure(
                                Throwable()
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getArtistsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ARTISTS -> if (offset == 0) Result.success(artistsPage1) else Result.failure(
                                Throwable()
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                )

                turbineScope {
                    viewModel.searchMusicsUiState.test {
                        skipItems(1) //  Skip init event

                        val previous = viewModel.searchMusicsUiState.value.userResearchType
                        if (type != previous) {
                            viewModel.onResearchTypeChanged(type)
                            awaitItem().apply {
                                assertEquals(expected = type, actual = userResearchType)
                                assertEquals(expected = "", actual = userResearch)
                                assertEquals(Idle, researchStatus)
                            }
                        }

                        viewModel.onUserResearchUpdated(shakiraInput)
                        assertEquals(shakiraInput, awaitItem().userResearch)
                        assertEquals(Loading, awaitItem().researchStatus)

                        val success1 = awaitItem().researchStatus as Success
                        val size1 = when (type) {
                            ResearchType.MUSICS -> (success1.items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> (success1.items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> (success1.items as Success.SearchItem.Artists).artists.size
                        }

                        viewModel.loadNextPage()
                        assertTrue(awaitItem().isPaging)

                        val after = awaitItem()
                        assertFalse(after.isPaging)
                        assertTrue(after.isPagingError)
                        assertFalse(after.isEndReached)

                        val success2 = after.researchStatus as Success
                        val size2 = when (type) {
                            ResearchType.MUSICS -> (success2.items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> (success2.items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> (success2.items as Success.SearchItem.Artists).artists.size
                        }
                        assertEquals(size1, size2)

                        expectNoEvents()
                    }
                }
            }
        }

    @Test
    fun `Should ignore loadNextPage reentrancy and append only once for all researchType`() =
        runTest {
            ResearchType.entries.forEach { type ->
                val page1Songs = randomSongs(count = 6)
                val page2Songs = randomSongs(count = 6)
                val page1Albums = randomAlbums(count = 6)
                val page2Albums = randomAlbums(count = 6)
                val page1Artists = randomArtists(count = 6)
                val page2Artists = randomArtists(count = 6)

                val viewModel = SearchMusicsViewModel(
                    savedStateHandle = SavedStateHandle(),
                    getSongsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.MUSICS -> if (offset == 0) Result.success(page1Songs) else Result.success(
                                page2Songs
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getAlbumsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ALBUMS -> if (offset == 0) Result.success(page1Albums) else Result.success(
                                page2Albums
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getArtistsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ARTISTS -> if (offset == 0) Result.success(page1Artists) else Result.success(
                                page2Artists
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                )

                turbineScope {
                    viewModel.searchMusicsUiState.test {
                        skipItems(1) // Skip init event

                        val prev = viewModel.searchMusicsUiState.value.userResearchType
                        if (type != prev) {
                            viewModel.onResearchTypeChanged(type)
                            awaitItem().apply {
                                assertEquals(expected = type, actual = userResearchType)
                                assertEquals(expected = "", actual = userResearch)
                                assertEquals(Idle, researchStatus)
                            }
                        }

                        viewModel.onUserResearchUpdated(shakiraInput)
                        assertEquals(shakiraInput, awaitItem().userResearch)
                        assertEquals(Loading, awaitItem().researchStatus)
                        val first = awaitItem()
                        val size1 = when (type) {
                            ResearchType.MUSICS -> (first.researchStatus as Success).let { (it.items as Success.SearchItem.Songs).songs.size }
                            ResearchType.ALBUMS -> (first.researchStatus as Success).let { (it.items as Success.SearchItem.Albums).albums.size }
                            ResearchType.ARTISTS -> (first.researchStatus as Success).let { (it.items as Success.SearchItem.Artists).artists.size }
                        }

                        viewModel.loadNextPage()
                        viewModel.loadNextPage()

                        assertTrue(awaitItem().isPaging)

                        val after = awaitItem()
                        assertFalse(after.isPaging)
                        val size2 = when (type) {
                            ResearchType.MUSICS -> ((after.researchStatus as Success).items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> ((after.researchStatus as Success).items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> ((after.researchStatus as Success).items as Success.SearchItem.Artists).artists.size
                        }
                        val expectedAdd = when (type) {
                            ResearchType.MUSICS -> page2Songs.size
                            ResearchType.ALBUMS -> page2Albums.size
                            ResearchType.ARTISTS -> page2Artists.size
                        }
                        assertEquals(size1 + expectedAdd, size2)

                        expectNoEvents()
                    }
                }
            }
        }

    @Test
    fun `Should set endReached when next page only returns duplicates for all researchType`() =
        runTest {
            ResearchType.entries.forEach { type ->
                val page1Songs = randomSongs(count = 6)
                val page1Albums = randomAlbums(count = 6)
                val page1Artists = randomArtists(count = 6)

                val viewModel = SearchMusicsViewModel(
                    savedStateHandle = SavedStateHandle(),
                    getSongsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.MUSICS -> if (offset == 0) Result.success(page1Songs) else Result.success(
                                page1Songs
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getAlbumsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ALBUMS -> if (offset == 0) Result.success(page1Albums) else Result.success(
                                page1Albums
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getArtistsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ARTISTS -> if (offset == 0) Result.success(page1Artists) else Result.success(
                                page1Artists
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                )

                turbineScope {
                    viewModel.searchMusicsUiState.test {
                        skipItems(1) // Skip init event

                        val prev = viewModel.searchMusicsUiState.value.userResearchType
                        if (type != prev) {
                            viewModel.onResearchTypeChanged(type)
                            awaitItem().apply {
                                assertEquals(expected = type, actual = userResearchType)
                                assertEquals(expected = "", actual = userResearch)
                                assertEquals(Idle, researchStatus)
                            }
                        }

                        viewModel.onUserResearchUpdated(shakiraInput)
                        assertEquals(shakiraInput, awaitItem().userResearch)
                        assertEquals(Loading, awaitItem().researchStatus)

                        val first = awaitItem()
                        val size1 = when (type) {
                            ResearchType.MUSICS -> ((first.researchStatus as Success).items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> ((first.researchStatus as Success).items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> ((first.researchStatus as Success).items as Success.SearchItem.Artists).artists.size
                        }

                        viewModel.loadNextPage()
                        assertTrue(awaitItem().isPaging)

                        val after = awaitItem()
                        assertFalse(after.isPaging)
                        assertTrue(after.isEndReached)

                        val size2 = when (type) {
                            ResearchType.MUSICS -> ((after.researchStatus as Success).items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> ((after.researchStatus as Success).items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> ((after.researchStatus as Success).items as Success.SearchItem.Artists).artists.size
                        }
                        assertEquals(size1, size2)

                        expectNoEvents()
                    }
                }
            }
        }

    @Test
    fun `Should clear pagingError and append after retrying next page for all researchType`() =
        runTest {
            ResearchType.entries.forEach { type ->
                val page1Songs = randomSongs(count = 5)
                val page2Songs = randomSongs(count = 4)
                val page1Albums = randomAlbums(count = 5)
                val page2Albums = randomAlbums(count = 4)
                val page1Artists = randomArtists(count = 5)
                val page2Artists = randomArtists(count = 4)

                var failNextPage = true

                val viewModel = SearchMusicsViewModel(
                    savedStateHandle = SavedStateHandle(),
                    getSongsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.MUSICS -> if (offset == 0) Result.success(page1Songs)
                            else if (failNextPage) Result.failure(Throwable()) else Result.success(
                                page2Songs
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getAlbumsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ALBUMS -> if (offset == 0) Result.success(page1Albums)
                            else if (failNextPage) Result.failure(Throwable()) else Result.success(
                                page2Albums
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                    getArtistsUseCase = { _, offset ->
                        when (type) {
                            ResearchType.ARTISTS -> if (offset == 0) Result.success(page1Artists)
                            else if (failNextPage) Result.failure(Throwable()) else Result.success(
                                page2Artists
                            )

                            else -> Result.success(emptyList())
                        }
                    },
                )

                turbineScope {
                    viewModel.searchMusicsUiState.test {
                        skipItems(1) // Skip init event

                        val prev = viewModel.searchMusicsUiState.value.userResearchType
                        if (type != prev) {
                            viewModel.onResearchTypeChanged(type)
                            awaitItem().apply {
                                assertEquals(expected = type, actual = userResearchType)
                                assertEquals(expected = "", actual = userResearch)
                                assertEquals(Idle, researchStatus)
                            }
                        }

                        viewModel.onUserResearchUpdated(shakiraInput)
                        assertEquals(shakiraInput, awaitItem().userResearch)
                        assertEquals(Loading, awaitItem().researchStatus)

                        val first = awaitItem()
                        val size1 = when (type) {
                            ResearchType.MUSICS -> ((first.researchStatus as Success).items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> ((first.researchStatus as Success).items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> ((first.researchStatus as Success).items as Success.SearchItem.Artists).artists.size
                        }

                        viewModel.loadNextPage()
                        assertTrue(awaitItem().isPaging)

                        val afterFail = awaitItem()
                        assertFalse(afterFail.isPaging)
                        assertTrue(afterFail.isPagingError)
                        val sizeAfterFail = when (type) {
                            ResearchType.MUSICS -> ((afterFail.researchStatus as Success).items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> ((afterFail.researchStatus as Success).items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> ((afterFail.researchStatus as Success).items as Success.SearchItem.Artists).artists.size
                        }
                        assertEquals(size1, sizeAfterFail)

                        failNextPage = false
                        viewModel.loadNextPage()
                        assertTrue(awaitItem().isPaging)

                        val afterRetry = awaitItem()
                        assertFalse(afterRetry.isPaging)
                        assertFalse(afterRetry.isPagingError)

                        val sizeAfterRetry = when (type) {
                            ResearchType.MUSICS -> ((afterRetry.researchStatus as Success).items as Success.SearchItem.Songs).songs.size
                            ResearchType.ALBUMS -> ((afterRetry.researchStatus as Success).items as Success.SearchItem.Albums).albums.size
                            ResearchType.ARTISTS -> ((afterRetry.researchStatus as Success).items as Success.SearchItem.Artists).artists.size
                        }
                        val expectedAdd = when (type) {
                            ResearchType.MUSICS -> page2Songs.size
                            ResearchType.ALBUMS -> page2Albums.size
                            ResearchType.ARTISTS -> page2Artists.size
                        }
                        assertEquals(size1 + expectedAdd, sizeAfterRetry)

                        expectNoEvents()
                    }
                }
            }
        }

    @Test
    fun `Should mark endReached when first page is empty for all researchType`() = runTest {
        ResearchType.entries.forEach { type ->
            val viewModel = SearchMusicsViewModel(
                savedStateHandle = SavedStateHandle(),
                getSongsUseCase = { _, _ ->
                    if (type == ResearchType.MUSICS) Result.success(
                        emptyList()
                    ) else Result.success(emptyList())
                },
                getAlbumsUseCase = { _, _ ->
                    if (type == ResearchType.ALBUMS) Result.success(
                        emptyList()
                    ) else Result.success(emptyList())
                },
                getArtistsUseCase = { _, _ ->
                    if (type == ResearchType.ARTISTS) Result.success(
                        emptyList()
                    ) else Result.success(emptyList())
                },
            )

            turbineScope {
                viewModel.searchMusicsUiState.test {
                    skipItems(1) // Skip init event

                    val prev = viewModel.searchMusicsUiState.value.userResearchType
                    if (type != prev) {
                        viewModel.onResearchTypeChanged(type)
                        awaitItem().apply {
                            assertEquals(expected = type, actual = userResearchType)
                            assertEquals(expected = "", actual = userResearch)
                            assertEquals(Idle, researchStatus)
                        }
                    }

                    viewModel.onUserResearchUpdated(shakiraInput)
                    assertEquals(shakiraInput, awaitItem().userResearch)
                    assertEquals(Loading, awaitItem().researchStatus)

                    val after = awaitItem()
                    assertFalse(after.isPaging)
                    assertTrue(after.isEndReached)

                    val size = when (type) {
                        ResearchType.MUSICS -> ((after.researchStatus as Success).items as Success.SearchItem.Songs).songs.size
                        ResearchType.ALBUMS -> ((after.researchStatus as Success).items as Success.SearchItem.Albums).albums.size
                        ResearchType.ARTISTS -> ((after.researchStatus as Success).items as Success.SearchItem.Artists).artists.size
                    }
                    assertEquals(0, size)

                    expectNoEvents()
                }
            }
        }
    }
}

private fun initViewModel(
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
    isSuccess: Boolean = true,
): SearchMusicsViewModel {

    return SearchMusicsViewModel(
        savedStateHandle = savedStateHandle,
        getSongsUseCase = { _, _ ->
            if (isSuccess)
                Result.success(randomSongs())
            else
                Result.failure(Throwable())
        },
        getAlbumsUseCase = { _, _ ->
            if (isSuccess)
                Result.success(randomAlbums())
            else
                Result.failure(Throwable())
        },
        getArtistsUseCase = { _, _ ->
            if (isSuccess)
                Result.success(randomArtists())
            else
                Result.failure(Throwable())
        }
    )
}

private const val charlesAznavourInput = "Charles Aznavour"
private const val bobMarleyInput = "Bob Marley"
private const val shakiraInput = "Shakira"