package infomaniak.musicapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import infomaniak.musicapp.musicdetails.album.AlbumDetailsScreen
import infomaniak.musicapp.musicdetails.artist.ArtistDetailsScreen
import infomaniak.musicapp.musicdetails.song.SongDetailsScreen
import infomaniak.musicapp.searchmusics.model.Album
import infomaniak.musicapp.searchmusics.model.Artist
import infomaniak.musicapp.searchmusics.model.Song

internal const val SongDetailsRoute = "song_details"
internal const val AlbumDetailsRoute = "album_details"
internal const val ArtistDetailsRoute = "artist_details"

fun NavController.navigateToSongDetailsScreen(
    song: Song,
    navOptions: NavOptions? = null,
) {
    navigate(
        resId = requireNotNull(graph.findNode(SongDetailsRoute)?.id),
        args = bundleOf(songArg to song),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.songDetailsScreen() {
    composable(route = SongDetailsRoute) {
        SongDetailsScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun NavController.navigateToAlbumDetailsScreen(
    album: Album,
    navOptions: NavOptions? = null,
) {
    navigate(
        resId = requireNotNull(graph.findNode(AlbumDetailsRoute)?.id),
        args = bundleOf(albumArg to album),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.albumDetailsScreen() {
    composable(route = AlbumDetailsRoute) {
        AlbumDetailsScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun NavController.navigateToArtistDetailsScreen(
    artist: Artist,
    navOptions: NavOptions? = null,
) {
    navigate(
        resId = requireNotNull(graph.findNode(ArtistDetailsRoute)?.id),
        args = bundleOf(artistArg to artist),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.artistDetailsScreen() {
    composable(route = ArtistDetailsRoute) {
        ArtistDetailsScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}