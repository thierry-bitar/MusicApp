package infomaniak.musicapp.musicdetails.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
internal fun InfoChips(
    modifier: Modifier = Modifier,
    genre: String?,
    year: String?,
    durationMs: Int? = null,
    trackCount: Int? = null,
) {
    val chips = buildList {
        if (!genre.isNullOrBlank()) add(Icons.Filled.MusicNote to genre)
        if (!year.isNullOrBlank()) add(Icons.Filled.CalendarMonth to year)
        durationMs?.let { ms ->
            val label = rememberSaveable(ms) { formatDurationMs(ms) }
            add(Icons.Filled.AccessTime to label)
        }
        if (trackCount != null) add(Icons.AutoMirrored.Filled.QueueMusic to trackCount.toString())
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

private fun formatDurationMs(ms: Int): String { // TODO Extract that operation in VM ?
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%d:%02d".format(min, sec)
}