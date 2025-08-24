package infomaniak.musicapp.searchmusics.composables.paging

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import infomaniak.musicapp.R
import infomaniak.musicapp.searchmusics.composables.paging.PagingFooterState.EndReached
import infomaniak.musicapp.searchmusics.composables.paging.PagingFooterState.Error
import infomaniak.musicapp.searchmusics.composables.paging.PagingFooterState.Idle
import infomaniak.musicapp.searchmusics.composables.paging.PagingFooterState.Loading

@Composable
fun PagingFooter(
    loadNextPage: () -> Unit,
    isPaging: Boolean,
    shouldLoadMore: Boolean,
    isPagingError: Boolean,
    isEndReached: Boolean,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        modifier = modifier,
        targetState = when {
            (isPaging || shouldLoadMore) && !isEndReached && !isPagingError -> Loading
            isPagingError && !isPaging -> Error
            isEndReached -> EndReached
            else -> Idle
        }
    ) { state ->
        when (state) {
            Loading -> Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.loader_wording),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Error -> Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.paging_error_message),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(onClick = loadNextPage) {
                    Text(text = stringResource(R.string.action_retry))
                }
            }

            EndReached -> Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.end_of_list_wording),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Idle -> Unit
        }
    }
}

private enum class PagingFooterState { Loading, Error, EndReached, Idle }