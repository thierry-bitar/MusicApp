package com.infomaniak.searchmusic.getalbumsusecase

import com.infomaniak.searchmusic.model.Album
import com.infomaniak.searchmusic.randomAlbums
import com.infomaniak.searchmusic.usecases.GetAlbumsUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetAlbumsUseCaseTest {

    @Test
    fun `test when GetAlbumsUseCase succeeds`() = runTest {
        val albums = randomAlbums()

        val repository = object : FakeAlbumsRepository() {
            override suspend fun searchAlbums(term: String, offset: Int): Result<List<Album>> =
                Result.success(albums)

        }
        val useCase = GetAlbumsUseCaseImpl(repository = repository)

        val result = useCase("x", 0)

        assertTrue(result.isSuccess)
        assertEquals(expected = albums, actual = result.getOrNull())
    }

    @Test
    fun `test when GetAlbumsUseCase fail`() = runTest {
        val repository = object : FakeAlbumsRepository() {
            override suspend fun searchAlbums(term: String, offset: Int): Result<List<Album>> =
                Result.failure(Throwable())

        }
        val useCase = GetAlbumsUseCaseImpl(repository = repository)

        val result = useCase("x", 0)

        assertTrue(result.isFailure)
        assertNull(result.getOrNull())
    }
}