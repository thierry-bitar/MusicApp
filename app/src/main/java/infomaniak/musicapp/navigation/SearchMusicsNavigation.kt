package infomaniak.musicapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import infomaniak.musicapp.searchmusics.composables.SearchMusicsScreen
import infomaniak.musicapp.searchmusics.model.Album
import infomaniak.musicapp.searchmusics.model.Artist
import infomaniak.musicapp.searchmusics.model.Song

internal const val SearchMusicsScreenRoute = "search_musics"

internal fun NavGraphBuilder.searchMusicsScreen(
    onSongClick: (Song) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onArtistClick: (Artist) -> Unit,
) {
    composable(
        route = SearchMusicsScreenRoute,
    ) {
        SearchMusicsScreen(
            modifier = Modifier.fillMaxSize(),
            onSongClick = onSongClick,
            onAlbumClick = onAlbumClick,
            onArtistClick = onArtistClick
        )
    }
}