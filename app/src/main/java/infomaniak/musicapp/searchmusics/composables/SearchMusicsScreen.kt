package infomaniak.musicapp.searchmusics.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import infomaniak.musicapp.R
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchType
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchType.ALBUMS
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchType.ARTISTS
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchType.MUSICS
import infomaniak.musicapp.searchmusics.composables.UiMode.Content
import infomaniak.musicapp.searchmusics.composables.UiMode.Error
import infomaniak.musicapp.searchmusics.composables.UiMode.Idle
import infomaniak.musicapp.searchmusics.composables.UiMode.Loading
import infomaniak.musicapp.searchmusics.composables.paging.albums.Albums
import infomaniak.musicapp.searchmusics.composables.paging.artists.Artists
import infomaniak.musicapp.searchmusics.composables.paging.songs.Songs
import infomaniak.musicapp.searchmusics.model.Album
import infomaniak.musicapp.searchmusics.model.Artist
import infomaniak.musicapp.searchmusics.model.Song

private enum class UiMode { Content, Loading, Error, Idle }

@Composable
fun SearchMusicsScreen(
    onSongClick: (Song) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onArtistClick: (Artist) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchMusicsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.searchMusicsUiState.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchMusicsFilterChips(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                selected = uiState.userResearchType,
                onSelectedChange = viewModel::onResearchTypeChanged
            )

            Spacer(Modifier.height(8.dp))

            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                userResearch = uiState.userResearch,
                userResearchType = uiState.userResearchType,
                onUserResearchUpdated = viewModel::onUserResearchUpdated,
                resetUserResearchField = viewModel::resetUserResearchField
            )

            Spacer(Modifier.height(16.dp))
            val listState = rememberLazyListState()

            LaunchedEffect(uiState.researchStatus) {
                if (uiState.researchStatus is ResearchStatus.Loading) {
                    listState.scrollToItem(0)
                }
            }

            Crossfade(
                targetState = when (uiState.researchStatus) {
                    is ResearchStatus.Success -> Content
                    ResearchStatus.Loading -> Loading
                    ResearchStatus.Error -> Error
                    ResearchStatus.Idle -> Idle
                },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                modifier = Modifier.fillMaxWidth()
            ) { researchStatus ->
                when (researchStatus) {
                    Content -> {
                        val successStatus = uiState.researchStatus as? ResearchStatus.Success
                            ?: return@Crossfade

                        when (val items = successStatus.items) {
                            is ResearchStatus.Success.SearchItem.Songs -> if (items.songs.isNotEmpty())
                                Songs(
                                    modifier = modifier.fillMaxWidth(),
                                    items = items,
                                    listState = listState,
                                    loadNextPage = viewModel::loadNextPage,
                                    isPaging = uiState.isPaging,
                                    isPagingError = uiState.isPagingError,
                                    isEndReached = uiState.isEndReached,
                                    onSongClick = onSongClick,
                                ) else EmptyResultScreen(modifier = Modifier.fillMaxSize())

                            is ResearchStatus.Success.SearchItem.Albums -> if (items.albums.isNotEmpty())
                                Albums(
                                    modifier = modifier.fillMaxWidth(),
                                    items = items,
                                    listState = listState,
                                    loadNextPage = viewModel::loadNextPage,
                                    isPaging = uiState.isPaging,
                                    isPagingError = uiState.isPagingError,
                                    isEndReached = uiState.isEndReached,
                                    onAlbumClick = onAlbumClick,
                                ) else EmptyResultScreen(modifier = Modifier.fillMaxSize())

                            is ResearchStatus.Success.SearchItem.Artists -> if (items.artists.isNotEmpty())
                                Artists(
                                    modifier = modifier.fillMaxWidth(),
                                    items = items,
                                    listState = listState,
                                    loadNextPage = viewModel::loadNextPage,
                                    isPaging = uiState.isPaging,
                                    isPagingError = uiState.isPagingError,
                                    isEndReached = uiState.isEndReached,
                                    onArtistClick = onArtistClick,
                                ) else EmptyResultScreen(modifier = Modifier.fillMaxSize())
                        }
                    }

                    Loading -> LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )

                    Error -> ErrorScreen(
                        modifier = Modifier.fillMaxSize(),
                        onRetry = viewModel::retry
                    )

                    Idle -> IdleScreen(
                        modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchMusicsFilterChips(
    modifier: Modifier = Modifier,
    selected: ResearchType,
    onSelectedChange: (ResearchType) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        listOf(MUSICS, ALBUMS, ARTISTS).forEach { researchType ->
            FilterChip(
                selected = selected == researchType,
                onClick = { onSelectedChange(researchType) },
                label = {
                    when (researchType) {
                        MUSICS -> Text(stringResource(R.string.research_type_musics))
                        ARTISTS -> Text(stringResource(R.string.research_type_artists))
                        ALBUMS -> Text(stringResource(R.string.research_type_albums))
                    }
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = FilterChipDefaults.filterChipColors()
            )
        }
    }
}

@Composable
private fun ColumnScope.SearchBar(
    modifier: Modifier = Modifier,
    userResearch: String,
    userResearchType: ResearchType,
    onUserResearchUpdated: (String) -> Unit,
    resetUserResearchField: () -> Unit,
) {
    TextField(
        modifier = modifier,
        value = userResearch,
        placeholder = {
            Text(
                when (userResearchType) {
                    MUSICS -> stringResource(R.string.search_placeholder_musics)
                    ARTISTS -> stringResource(R.string.search_placeholder_artists)
                    ALBUMS -> stringResource(R.string.search_placeholder_albums)
                }
            )
        },
        onValueChange = onUserResearchUpdated,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = userResearch.isNotEmpty()) {
                IconButton(onClick = resetUserResearchField) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = null,
                    )
                }
            }
        },
        maxLines = 1,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.50f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}
