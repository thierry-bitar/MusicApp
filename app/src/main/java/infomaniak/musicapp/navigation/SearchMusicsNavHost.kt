package infomaniak.musicapp.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun SearchMusicsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = SearchMusicsScreenRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        searchMusicsScreen(
            onSongClick = { song -> navController.navigateToSongDetailsScreen(song = song) },
            onAlbumClick = { album -> navController.navigateToAlbumDetailsScreen(album = album) },
            onArtistClick = { artist -> navController.navigateToArtistDetailsScreen(artist = artist) }
        )

        songDetailsScreen()
        albumDetailsScreen()
        artistDetailsScreen()
    }
}

internal const val songArg = "songArg"
internal const val albumArg = "albumArg"
internal const val artistArg = "artistArg"
