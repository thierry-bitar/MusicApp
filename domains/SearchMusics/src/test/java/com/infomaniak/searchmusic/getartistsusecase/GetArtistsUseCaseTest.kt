package com.infomaniak.searchmusic.getartistsusecase

import com.infomaniak.searchmusic.model.Artist
import com.infomaniak.searchmusic.randomArtists
import com.infomaniak.searchmusic.usecases.GetArtistsUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetArtistsUseCaseTest {

    @Test
    fun `test when GetArtistsUseCase succeeds`() = runTest {
        val artists = randomArtists()

        val repository = object : FakeArtistsRepository() {
            override suspend fun searchArtists(term: String, offset: Int): Result<List<Artist>> =
                Result.success(artists)

        }
        val useCase = GetArtistsUseCaseImpl(repository = repository)

        val result = useCase("x", 0)

        assertTrue(result.isSuccess)
        assertEquals(expected = artists, actual = result.getOrNull())
    }

    @Test
    fun `test when GetArtistsUseCase fail`() = runTest {
        val repository = object : FakeArtistsRepository() {
            override suspend fun searchArtists(term: String, offset: Int): Result<List<Artist>> =
                Result.failure(Throwable())

        }
        val useCase = GetArtistsUseCaseImpl(repository = repository)

        val result = useCase("x", 0)

        assertTrue(result.isFailure)
        assertNull(result.getOrNull())
    }
}