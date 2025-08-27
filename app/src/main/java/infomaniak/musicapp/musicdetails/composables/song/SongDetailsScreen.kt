package infomaniak.musicapp.musicdetails.composables.song

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import infomaniak.musicapp.musicdetails.SongDetailsViewModel
import infomaniak.musicapp.musicdetails.composables.ExternalLinksSection
import infomaniak.musicapp.musicdetails.composables.InfoChips

@Composable
fun SongDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: SongDetailsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.songDetailsUiState.collectAsStateWithLifecycle().value

    SongDetailsScreen(
        uiState = uiState,
        onPlay = viewModel::play,
        onPause = viewModel::pause,
        onSeekTo = viewModel::seekTo,
        onReplay = viewModel::replay,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongDetailsScreen(
    uiState: SongDetailsViewModel.SongDetailsUiState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onReplay: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                model = uiState.song.artworkUrl100,
                contentDescription = null
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = uiState.song.trackName,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = uiState.song.artistName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = uiState.song.collectionName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))
            InfoChips(
                modifier = Modifier.fillMaxWidth(),
                genre = uiState.song.primaryGenreName,
                year = uiState.song.releaseYear,
                durationMs = uiState.song.trackTimeMillis
            )

            Spacer(Modifier.height(16.dp))
            uiState.song.previewUrl?.let {
                AudioPlayerControls(
                    modifier = Modifier.fillMaxWidth(),
                    trackName = uiState.song.trackName,
                    isPlaying = uiState.playerState.isPlaying,
                    positionMs = uiState.playerState.positionMs,
                    durationMs = uiState.playerState.durationMs,
                    isEndReached = uiState.playerState.isEndReached,
                    isError = uiState.playerState.error,
                    isLoading = uiState.playerState.isLoading,
                    onPlay = onPlay,
                    onPause = onPause,
                    onSeekTo = onSeekTo,
                    onRetry = onRetry,
                    onReplay = onReplay,
                )
            }

            Spacer(Modifier.height(16.dp))
            ExternalLinksSection(
                modifier = Modifier.fillMaxWidth(),
                trackUrl = uiState.song.trackViewUrl,
                artistUrl = uiState.song.artistViewUrl,
                collectionUrl = uiState.song.collectionViewUrl,
                collectionArtistUrl = uiState.song.collectionArtistViewUrl,
            )
        }
    }
}