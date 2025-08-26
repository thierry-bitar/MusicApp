package infomaniak.musicapp.musicdetails.composables.song

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import infomaniak.musicapp.R

@Composable
fun AudioPlayerControls(
    trackName: String,
    isPlaying: Boolean,
    positionMs: Long,
    durationMs: Long,
    isEndReached: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onReplay: () -> Unit,
    onRetry: () -> Unit,
    isError: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    var isUserScrubbing by rememberSaveable { mutableStateOf(false) }
    var scrubbingPosition by rememberSaveable { mutableLongStateOf(positionMs) }

    val total = if (durationMs <= 0L) 1L else durationMs
    val sliderValue = when {
        isUserScrubbing -> scrubbingPosition.coerceIn(0L, total).toFloat()
        else -> positionMs.coerceIn(0L, total).toFloat()
    }

    ElevatedCard(modifier = modifier) {
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
        AnimatedVisibility(
            visible = isError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(R.string.action_retry_music_player),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (isError) {
                FilledIconButton(
                    onClick = onRetry,
                    enabled = !isLoading
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh, contentDescription = stringResource(
                            id = R.string.action_retry
                        )
                    )
                }
            } else {
                if (isEndReached) {
                    FilledIconButton(onClick = onReplay) {
                        Icon(
                            imageVector = Icons.Filled.Refresh, contentDescription = stringResource(
                                id = R.string.player_restart_label
                            )
                        )
                    }
                } else {
                    FilledIconButton(
                        onClick = { if (isPlaying) onPause() else onPlay() },
                        enabled = !isLoading
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying)
                                stringResource(R.string.player_pause_label)
                            else stringResource(R.string.player_start_label)
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.trackName, trackName),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = if (isPlaying) Modifier.basicMarquee() else Modifier
                )
                Spacer(Modifier.height(8.dp))

                Slider(
                    value = sliderValue,
                    onValueChange = {
                        isUserScrubbing = true
                        scrubbingPosition = it.toLong()
                    },
                    onValueChangeFinished = {
                        isUserScrubbing = false
                        onSeekTo(scrubbingPosition)
                    },
                    valueRange = 0f..total.toFloat(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        thumbColor = MaterialTheme.colorScheme.primary
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatMillis(if (isUserScrubbing) scrubbingPosition else positionMs),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(formatMillis(durationMs), style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }

    val appLifecycleOwner = remember { ProcessLifecycleOwner.get() }
    LifecycleEventEffect(Lifecycle.Event.ON_STOP, appLifecycleOwner) {
        onPause()
    }
}

private fun formatMillis(ms: Long): String {
    val totalSec = (ms / 1000).coerceAtLeast(0)
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%d:%02d".format(min, sec)
}