package infomaniak.musicapp.musicdetails.composables.song

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import infomaniak.musicapp.R
import infomaniak.musicapp.musicdetails.SongDetailsViewModel
import infomaniak.musicapp.musicdetails.composables.AudioPlayerBar

@Composable
fun SongDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: SongDetailsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.songDetailsUiState.collectAsStateWithLifecycle().value

    SongDetailsScreen(
        uiState = uiState,
        onPlay = viewModel::onPlay,
        onPause = viewModel::onPause,
        onSeekTo = viewModel::onSeekTo,
        onReplay = viewModel::onReplay,
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
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

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
                model = ImageRequest.Builder(context)
                    .data(uiState.song.artworkUrl100)
                    .crossfade(true)
                    .build(),
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
            // TODO Extract year in Repo properly with a parser
            DetailsChips(
                modifier = Modifier.fillMaxWidth(),
                genre = uiState.song.primaryGenreName,
                year = uiState.song.releaseDate?.takeIf { it.length >= 4 }?.substring(0, 4),
                durationMs = uiState.song.trackTimeMillis
            )

            Spacer(Modifier.height(16.dp))
            uiState.song.previewUrl?.let {
                AudioPlayerBar(
                    modifier = Modifier.fillMaxWidth(),
                    trackName = uiState.song.trackName,
                    isPlaying = uiState.playerState.isPlaying,
                    positionMs = uiState.playerState.positionMs,
                    durationMs = uiState.playerState.durationMs,
                    isEndReached = uiState.playerState.isEndReached,
                    onPlay = onPlay,
                    onPause = onPause,
                    onSeekTo = onSeekTo,
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

@Composable
private fun DetailsChips(
    genre: String?,
    year: String?,
    durationMs: Int?,
    modifier: Modifier = Modifier,
) {
    val chips = buildList {
        if (!genre.isNullOrBlank()) add(Icons.Filled.MusicNote to genre)
        if (!year.isNullOrBlank()) add(Icons.Filled.CalendarMonth to year)
        durationMs?.let { ms ->
            val label = rememberSaveable(ms) { formatDurationMs(ms) }
            add(Icons.Filled.AccessTime to label)
        }
    }
    if (chips.isEmpty()) return

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { (icon, label) ->
            AssistChip(
                onClick = {},
                enabled = false,
                leadingIcon = { Icon(icon, contentDescription = null) },
                label = {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExternalLinksSection(
    trackUrl: String?,
    artistUrl: String?,
    collectionUrl: String?,
    collectionArtistUrl: String?,
    modifier: Modifier = Modifier,
) {
    val items = listOfNotNull(
        trackUrl?.let {
            Triple(
                stringResource(R.string.external_link_track),
                Icons.Outlined.LibraryMusic, it
            )
        },
        artistUrl?.let {
            Triple(
                stringResource(R.string.external_link_artist),
                Icons.Outlined.Person,
                it
            )
        },
        collectionUrl?.let {
            Triple(
                stringResource(R.string.external_link_album),
                Icons.Outlined.Album,
                it
            )
        },
        collectionArtistUrl?.let {
            Triple(
                stringResource(R.string.external_link_album_artist),
                Icons.Outlined.Person,
                it
            )
        },
    )
    if (items.isEmpty()) return

    val uriHandler = LocalUriHandler.current

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { (label, icon, url) ->
            FilledTonalButton(
                onClick = { uriHandler.openUri(url) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(icon, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(text = label)
            }
        }
    }
}

private fun formatDurationMs(ms: Int): String { // TODO Extract that operation in VM ?
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%d:%02d".format(min, sec)
}