package infomaniak.musicapp.searchmusics.composables.paging.artists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus
import infomaniak.musicapp.searchmusics.composables.paging.PagingFooter
import infomaniak.musicapp.searchmusics.model.Artist

@Composable
internal fun Artists(
    onArtistClick: (Artist) -> Unit,
    items: ResearchStatus.Success.SearchItem.Artists,
    listState: LazyListState,
    loadNextPage: () -> Unit,
    isPaging: Boolean,
    isPagingError: Boolean,
    isEndReached: Boolean,
    modifier: Modifier = Modifier,
) {
    val shouldLoadMore by remember(listState) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            total > 0 && lastVisible >= total - 5
        }
    }
    LaunchedEffect(shouldLoadMore, isPaging, isEndReached, isPagingError) {
        if (shouldLoadMore && !isPaging && !isEndReached && !isPagingError) loadNextPage()
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items.artists,
            key = { it.artistId }
        ) { artist ->
            Artist(
                artist = artist,
                onClick = { onArtistClick(artist) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            PagingFooter(
                loadNextPage = loadNextPage,
                isPaging = isPaging,
                shouldLoadMore = shouldLoadMore,
                isPagingError = isPagingError,
                isEndReached = isEndReached,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }
}