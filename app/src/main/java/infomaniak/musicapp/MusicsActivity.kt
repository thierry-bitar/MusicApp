package infomaniak.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import infomaniak.musicapp.navigation.SearchMusicsNavHost
import infomaniak.musicapp.searchmusics.composables.SearchMusicsScreen
import infomaniak.musicapp.ui.theme.MusicAppTheme

@AndroidEntryPoint
class SearchMusicsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                val navController = rememberNavController()

                SearchMusicsNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMusicsScreenPreview() {
    MusicAppTheme {
        SearchMusicsScreen(
            modifier = Modifier.fillMaxSize(),
            onArtistClick = {},
            onAlbumClick = {},
            onSongClick = {},
        )
    }
}