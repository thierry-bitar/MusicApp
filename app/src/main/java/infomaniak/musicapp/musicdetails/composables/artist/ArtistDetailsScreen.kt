package infomaniak.musicapp.musicdetails.composables.artist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import infomaniak.musicapp.musicdetails.ArtistDetailsViewModel
import infomaniak.musicapp.musicdetails.composables.ExternalLinksSection

@Composable
fun ArtistDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: ArtistDetailsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.artistDetailsUiState.collectAsStateWithLifecycle().value

    ArtistDetailsScreen(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
private fun ArtistDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: ArtistDetailsViewModel.ArtistDetailsUiState,
) {
    Scaffold(
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.artist.artistName,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            uiState.artist.primaryGenreName?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(16.dp))
            ExternalLinksSection(
                artistUrl = uiState.artist.artistLinkUrl
            )
        }
    }
}