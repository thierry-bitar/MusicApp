package infomaniak.musicapp.searchmusics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import infomaniak.musicapp.searchmusics.composables.SearchMusicsScreen
import infomaniak.musicapp.ui.theme.MusicAppTheme

@AndroidEntryPoint
class SearchMusicsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                SearchMusicsScreen(
                    modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.fillMaxSize()
        )
    }
}