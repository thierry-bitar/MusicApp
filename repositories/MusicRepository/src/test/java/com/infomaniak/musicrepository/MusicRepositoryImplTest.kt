package com.infomaniak.musicrepository

import com.infomaniak.musicrepository.mapper.toAlbumFromDomains
import com.infomaniak.musicrepository.mapper.toArtistFromDomains
import com.infomaniak.musicrepository.mapper.toSongFromDomains
import com.infomaniak.musicrepository.model.SearchAlbumsResponse
import com.infomaniak.musicrepository.model.SearchArtistsResponse
import com.infomaniak.musicrepository.model.SearchSongsResponse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
class MusicRepositoryImplTest {

    @Test
    fun `searchSongs should return Result success with songs when api succeed`() = runTest {
        val page = randomSongs(count = 5)

        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = {
                    SearchSongsResponse(
                        resultCount = page.size,
                        results = page
                    )
                },
                onSearchArtists = { -> error("not used here") },
                onSearchAlbums = { -> error("not used here") },
            )
        )

        val result = repository.searchSongs(
            term = "x",
            offset = Random.nextInt()
        )

        assertTrue(result.isSuccess)
        val value = result.getOrNull()
        assertNotNull(value)
        assertEquals(expected = page.map { it.toSongFromDomains() }, actual = value)
    }

    @Test
    fun `searchSongs should return Result failure when api fail`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { throw Throwable() },
                onSearchArtists = { -> error("not used here") },
                onSearchAlbums = { -> error("not used here") },
            )
        )

        val result = repository.searchSongs(
            term = "x",
            offset = Random.nextInt()
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun `searchSongs returns success empty list on 404`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { throw http404() },
                onSearchArtists = { error("not used") },
                onSearchAlbums = { error("not used") },
            )
        )

        val result = repository.searchSongs(term = "x", offset = 0)

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrNull())
    }

    @Test
    fun `searchSongs returns failure on non-404 HttpException`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { throw http500() },
                onSearchArtists = { error("not used") },
                onSearchAlbums = { error("not used") },
            )
        )

        val result = repository.searchSongs(term = "x", offset = 0)

        assertTrue(result.isFailure)
    }

    @Test
    fun `searchAlbums should return Result success with albums when api succeed`() = runTest {
        val page = randomAlbums(count = 5)

        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used here") },
                onSearchArtists = { -> error("not used here") },
                onSearchAlbums = { ->
                    SearchAlbumsResponse(
                        resultCount = page.size,
                        results = page
                    )
                },
            )
        )

        val result = repository.searchAlbums(
            term = "x",
            offset = Random.nextInt()
        )

        assertTrue(result.isSuccess)
        val value = result.getOrNull()
        assertNotNull(value)
        assertEquals(expected = page.map { it.toAlbumFromDomains() }, actual = value)
    }

    @Test
    fun `searchAlbums should return Result failure when api fail`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used here") },
                onSearchArtists = { -> error("not used here") },
                onSearchAlbums = { -> throw Throwable() },
            )
        )

        val result = repository.searchAlbums(
            term = "x",
            offset = Random.nextInt()
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun `searchAlbums returns success empty list on 404`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used") },
                onSearchArtists = { error("not used") },
                onSearchAlbums = { throw http404() },
            )
        )

        val result = repository.searchAlbums(term = "x", offset = 0)

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrNull())
    }

    @Test
    fun `searchAlbums returns failure on non-404 HttpException`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used") },
                onSearchArtists = { error("not used") },
                onSearchAlbums = { throw http500() },
            )
        )

        val result = repository.searchAlbums(term = "x", offset = 0)

        assertTrue(result.isFailure)
    }

    @Test
    fun `searchArtists should return Result success with artists when api succeed`() = runTest {
        val page = randomArtists(count = 5)

        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used here") },
                onSearchArtists = { ->
                    SearchArtistsResponse(
                        resultCount = page.size,
                        results = page
                    )
                },
                onSearchAlbums = { -> error("not used here") },
            )
        )

        val result = repository.searchArtists(
            term = "x",
            offset = Random.nextInt()
        )

        assertTrue(result.isSuccess)
        val value = result.getOrNull()
        assertNotNull(value)
        assertEquals(expected = page.map { it.toArtistFromDomains() }, actual = value)
    }

    @Test
    fun `searchArtists should return Result failure when api fail`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used here") },
                onSearchArtists = { -> throw Throwable() },
                onSearchAlbums = { -> error("not used here") },
            )
        )

        val result = repository.searchArtists(
            term = "x",
            offset = Random.nextInt()
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun `searchArtists returns success empty list on 404`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used") },
                onSearchArtists = { throw http404() },
                onSearchAlbums = { error("not used") },
            )
        )

        val result = repository.searchArtists(term = "x", offset = 0)

        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrNull())
    }

    @Test
    fun `searchArtists returns failure on non-404 HttpException`() = runTest {
        val repository = MusicRepositoryImpl(
            musicApiService = FakeMusicApiService(
                onSearchSongs = { error("not used") },
                onSearchArtists = { throw http500() },
                onSearchAlbums = { error("not used") },
            )
        )

        val result = repository.searchArtists(term = "x", offset = 0)

        assertTrue(result.isFailure)
    }
}