package com.infomaniak.musicrepository

import com.infomaniak.musicrepository.model.Album
import com.infomaniak.musicrepository.model.Artist
import com.infomaniak.musicrepository.model.Song
import kotlin.random.Random

fun randomArtist() = Artist(
    artistId = Random.nextLong(),
    artistName = Random.nextInt().toString(),
    artistLinkUrl = Random.nextInt().toString(),
    primaryGenreName = Random.nextInt().toString()
)

fun randomArtists(count: Int = 20): List<Artist> =
    List(count) {
        randomArtist()
    }

fun randomAlbum() = Album(
    artistId = Random.nextLong(),
    collectionId = Random.nextLong(),
    collectionName = Random.nextInt().toString(),
    artistName = Random.nextInt().toString(),
    artistViewUrl = Random.nextInt().toString(),
    collectionViewUrl = Random.nextInt().toString(),
    artworkUrl100 = Random.nextInt().toString(),
    trackCount = Random.nextInt(),
    primaryGenreName = Random.nextInt().toString(),
    releaseDate = Random.nextInt().toString()
)

fun randomAlbums(count: Int = 20): List<Album> =
    List(count) {
        randomAlbum()
    }

fun randomSongs(count: Int = 20): List<Song> =
    List(count) {
        randomSong()
    }

fun randomSong() = Song(
    trackId = Random.nextLong(),
    artistId = Random.nextLong(),
    collectionId = Random.nextLong(),
    artistName = Random.nextInt().toString(),
    collectionName = Random.nextInt().toString(),
    trackName = Random.nextInt().toString(),
    trackViewUrl = Random.nextInt().toString(),
    previewUrl = Random.nextInt().toString(),
    artistViewUrl = Random.nextInt().toString(),
    collectionViewUrl = Random.nextInt().toString(),
    collectionArtistViewUrl = Random.nextInt().toString(),
    primaryGenreName = Random.nextInt().toString(),
    releaseDate = Random.nextInt().toString(),
    artworkUrl100 = Random.nextInt().toString(),
    trackTimeMillis = Random.nextInt()
)