package infomaniak.musicapp.musicdetails.song

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SongDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: SongDetailsViewModel = hiltViewModel(),
) {
    Text("SongDetailScreen")
}