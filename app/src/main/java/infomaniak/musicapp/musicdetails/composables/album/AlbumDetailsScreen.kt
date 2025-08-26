package infomaniak.musicapp.musicdetails.composables.album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import infomaniak.musicapp.musicdetails.AlbumDetailsViewModel
import infomaniak.musicapp.musicdetails.composables.InfoChips
import infomaniak.musicapp.musicdetails.composables.ExternalLinksSection

@Composable
fun AlbumDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: AlbumDetailsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.albumDetailsUiState.collectAsStateWithLifecycle().value

    AlbumDetailsScreen(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
private fun AlbumDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: AlbumDetailsViewModel.AlbumDetailsUiState,
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
                model = uiState.album.artworkUrl100,
                contentDescription = null
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = uiState.album.collectionName,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = uiState.album.artistName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))
            // TODO Extract year in Repo properly with a parser
            InfoChips(
                modifier = Modifier.fillMaxWidth(),
                genre = uiState.album.primaryGenreName,
                year = uiState.album.releaseDate?.takeIf { it.length >= 4 }?.substring(0, 4),
                trackCount = uiState.album.trackCount
            )

            Spacer(Modifier.height(16.dp))
            ExternalLinksSection(
                modifier = Modifier.fillMaxWidth(),
                artistUrl = uiState.album.artistViewUrl,
                collectionUrl = uiState.album.collectionViewUrl,
            )
        }
    }
}