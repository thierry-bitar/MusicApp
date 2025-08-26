package infomaniak.musicapp.musicdetails.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import infomaniak.musicapp.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ExternalLinksSection(
    modifier: Modifier = Modifier,
    trackUrl: String? = null,
    artistUrl: String? = null,
    collectionUrl: String? = null,
    collectionArtistUrl: String? = null,
) {
    val items = listOfNotNull(
        trackUrl?.let {
            Triple(
                first = stringResource(id = R.string.external_link_track),
                second = Icons.Outlined.LibraryMusic, third = it
            )
        },
        artistUrl?.let {
            Triple(
                first = stringResource(id = R.string.external_link_artist),
                second = Icons.Outlined.Person,
                third = it
            )
        },
        collectionUrl?.let {
            Triple(
                first = stringResource(id = R.string.external_link_album),
                second = Icons.Outlined.Album,
                third = it
            )
        },
        collectionArtistUrl?.let {
            Triple(
                first = stringResource(id = R.string.external_link_album_artist),
                second = Icons.Outlined.Person,
                third = it
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
                onClick = { uriHandler.openUri(uri = url) },
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