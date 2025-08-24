package com.infomaniak.searchmusic.getsongsusecase

import com.infomaniak.searchmusic.model.Song
import com.infomaniak.searchmusic.randomSongs
import com.infomaniak.searchmusic.usecases.GetSongsUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetSongsUseCaseTest {

    @Test
    fun `test when GetSongsUseCase succeeds`() = runTest {
        val songs = randomSongs()

        val repository = object : FakeSongsRepository() {
            override suspend fun searchSongs(term: String, offset: Int): Result<List<Song>> =
                Result.success(songs)

        }
        val useCase = GetSongsUseCaseImpl(repository = repository)

        val result = useCase("x", 0)

        assertTrue(result.isSuccess)
        assertEquals(expected = songs, actual = result.getOrNull())
    }

    @Test
    fun `test when GetSongsUseCase fail`() = runTest {
        val repository = object : FakeSongsRepository() {
            override suspend fun searchSongs(term: String, offset: Int): Result<List<Song>> =
                Result.failure(Throwable())

        }
        val useCase = GetSongsUseCaseImpl(repository = repository)

        val result = useCase("x", 0)

        assertTrue(result.isFailure)
        assertNull(result.getOrNull())
    }
}