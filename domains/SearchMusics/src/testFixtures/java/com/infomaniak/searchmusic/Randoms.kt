package com.infomaniak.searchmusic

import com.infomaniak.searchmusic.model.Album
import com.infomaniak.searchmusic.model.Artist
import com.infomaniak.searchmusic.model.Song
import kotlin.random.Random

fun randomArtists(count: Int = 20): List<Artist> =
    List(count) {
        randomArtist()
    }

fun randomArtist() = Artist(
    artistId = Random.nextLong(1, Long.MAX_VALUE),
    artistName = Random.nextInt().toString(),
    artistLinkUrl = Random.nextInt().toString(),
    primaryGenreName = Random.nextInt().toString()
)

fun randomAlbums(count: Int = 20): List<Album> =
    List(count) {
        randomAlbum()
    }

fun randomAlbum() = Album(
    artistId = Random.nextLong(1, Long.MAX_VALUE),
    collectionId = Random.nextLong(1, Long.MAX_VALUE),
    collectionName = Random.nextInt().toString(),
    artistName = Random.nextInt().toString(),
    artistViewUrl = Random.nextInt().toString(),
    collectionViewUrl = Random.nextInt().toString(),
    artworkUrl100 = Random.nextInt().toString(),
    trackCount = Random.nextInt(1, 30),
    primaryGenreName = Random.nextInt().toString(),
    releaseDate = Random.nextInt().toString()
)

fun randomSongs(count: Int = 20): List<Song> =
    List(count) {
        randomSong()
    }

fun randomSong() = Song(
    trackId = Random.nextLong(1, Long.MAX_VALUE),
    artistId = Random.nextLong(1, Long.MAX_VALUE),
    collectionId = Random.nextLong(1, Long.MAX_VALUE),
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