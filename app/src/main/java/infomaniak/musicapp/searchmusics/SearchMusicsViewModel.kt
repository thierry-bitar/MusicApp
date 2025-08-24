package infomaniak.musicapp.searchmusics

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infomaniak.searchmusic.usecases.GetAlbumsUseCase
import com.infomaniak.searchmusic.usecases.GetArtistsUseCase
import com.infomaniak.searchmusic.usecases.GetSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus.Success
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchStatus.Success.SearchItem
import infomaniak.musicapp.searchmusics.SearchMusicsViewModel.SearchMusicsUiState.ResearchType
import infomaniak.musicapp.searchmusics.mapper.toAlbum
import infomaniak.musicapp.searchmusics.mapper.toArtist
import infomaniak.musicapp.searchmusics.mapper.toSong
import infomaniak.musicapp.searchmusics.model.Album
import infomaniak.musicapp.searchmusics.model.Artist
import infomaniak.musicapp.searchmusics.model.Song
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchMusicsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSongsUseCase: GetSongsUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getArtistsUseCase: GetArtistsUseCase
) : ViewModel() {

    val searchMusicsUiState: StateFlow<SearchMusicsUiState> = savedStateHandle.getStateFlow(
        key = SEARCH_MUSICS,
        initialValue = SearchMusicsUiState(
            researchStatus = ResearchStatus.Idle
        ),
    )

    init {
        viewModelScope.launch {
            searchMusicsUiState.map { it.userResearch }
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { lastResearch ->
                    if (lastResearch.isNotBlank()) {
                        savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                            researchStatus = ResearchStatus.Loading,
                            isPaging = false,
                            isPagingError = false,
                            isEndReached = false,
                        )

                        when (searchMusicsUiState.value.userResearchType) {
                            ResearchType.MUSICS -> searchSongs(input = lastResearch)
                            ResearchType.ARTISTS -> searchArtists(input = lastResearch)
                            ResearchType.ALBUMS -> searchAlbums(input = lastResearch)
                        }
                    } else {
                        savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                            researchStatus = ResearchStatus.Idle,
                            isPaging = false,
                            isPagingError = false,
                            isEndReached = false,
                        )
                    }
                }
        }
    }

    fun onUserResearchUpdated(input: String) {
        savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
            userResearch = input,
        )
    }

    private fun searchSongs(input: String) {
        viewModelScope.launch {
            val songsListSizeBeforePagingTrial = currentListSize(
                searchMusicsUiState.value.researchStatus
            )

            getSongsUseCase(
                term = input,
                offset = songsListSizeBeforePagingTrial,
            ).onSuccess { songs ->
                val newResearchStatus = appendPage(
                    status = searchMusicsUiState.value.researchStatus,
                    newItem = SearchItem.Songs(songs.map { it.toSong() })
                )
                val newListSize = currentListSize(newResearchStatus)

                savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                    researchStatus = newResearchStatus,
                    isPaging = false,
                    isEndReached = newListSize <= songsListSizeBeforePagingTrial,
                )
            }.onFailure { t ->
                val listSize = currentListSize(searchMusicsUiState.value.researchStatus)
                if (listSize > 0) {
                    savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                        isPaging = false,
                        isPagingError = true,
                    )
                } else {
                    savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                        researchStatus = ResearchStatus.Error,
                        isPaging = false,
                    )
                }
            }
        }
    }

    private fun searchAlbums(input: String) {
        viewModelScope.launch {
            val albumsListSizeBeforePagingTrial = currentListSize(
                searchMusicsUiState.value.researchStatus
            )

            getAlbumsUseCase(
                term = input,
                offset = albumsListSizeBeforePagingTrial,
            ).onSuccess { albums ->
                val newResearchStatus = appendPage(
                    status = searchMusicsUiState.value.researchStatus,
                    newItem = SearchItem.Albums(albums.map { it.toAlbum() })
                )
                val newListSize = currentListSize(newResearchStatus)

                savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                    researchStatus = newResearchStatus,
                    isPaging = false,
                    isEndReached = newListSize <= albumsListSizeBeforePagingTrial,
                )
            }.onFailure { t ->
                val listSize = currentListSize(searchMusicsUiState.value.researchStatus)
                if (listSize > 0) {
                    savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                        isPaging = false,
                        isPagingError = true,
                    )
                } else {
                    savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                        researchStatus = ResearchStatus.Error,
                        isPaging = false,
                    )
                }
            }
        }
    }

    private fun searchArtists(input: String) {
        viewModelScope.launch {
            val artistsListSizeBeforePagingTrial = currentListSize(
                searchMusicsUiState.value.researchStatus
            )

            getArtistsUseCase(
                term = input,
                offset = artistsListSizeBeforePagingTrial,
            ).onSuccess { artists ->
                val newResearchStatus = appendPage(
                    status = searchMusicsUiState.value.researchStatus,
                    newItem = SearchItem.Artists(artists = artists.map { it.toArtist() })
                )
                val newListSize = currentListSize(newResearchStatus)

                savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                    researchStatus = newResearchStatus,
                    isPaging = false,
                    isEndReached = newListSize <= artistsListSizeBeforePagingTrial,
                )
            }.onFailure { t ->
                val listSize = currentListSize(searchMusicsUiState.value.researchStatus)
                if (listSize > 0) {
                    savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                        isPaging = false,
                        isPagingError = true,
                    )
                } else {
                    savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                        researchStatus = ResearchStatus.Error,
                        isPaging = false,
                    )
                }
            }
        }
    }

    fun resetUserResearchField() {
        savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
            userResearch = String(),
            isPaging = false,
            isEndReached = false,
        )
    }

    fun onResearchTypeChanged(researchType: ResearchType) {
        if (researchType != searchMusicsUiState.value.userResearchType) {
            savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
                userResearchType = researchType,
                researchStatus = ResearchStatus.Idle,
                userResearch = String(),
                isPaging = false,
                isEndReached = false,
            )
        }
    }

    fun retry() {
        savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
            researchStatus = ResearchStatus.Loading,
        )

        when (searchMusicsUiState.value.userResearchType) {
            ResearchType.MUSICS -> searchSongs(input = searchMusicsUiState.value.userResearch)
            ResearchType.ALBUMS -> searchAlbums(input = searchMusicsUiState.value.userResearch)
            ResearchType.ARTISTS -> searchArtists(input = searchMusicsUiState.value.userResearch)
        }
    }

    fun loadNextPage() {
        val state = searchMusicsUiState.value
        val query = state.userResearch
        if (state.isPaging || state.isEndReached) return

        savedStateHandle[SEARCH_MUSICS] = searchMusicsUiState.value.copy(
            isPaging = true,
            isPagingError = false,
        )

        when (state.userResearchType) {
            ResearchType.MUSICS -> searchSongs(input = query)
            ResearchType.ALBUMS -> searchAlbums(input = query)
            ResearchType.ARTISTS -> searchArtists(input = query)
        }
    }

    private fun currentListSize(status: ResearchStatus): Int = when (status) {
        is Success -> when (val items = status.items) {
            is SearchItem.Songs -> items.songs.size
            is SearchItem.Albums -> items.albums.size
            is SearchItem.Artists -> items.artists.size
        }

        else -> 0
    }

    private fun appendPage(
        status: ResearchStatus,
        newItem: SearchItem
    ): ResearchStatus = when (status) {
        is Success -> {
            val merged: SearchItem = when (val current = status.items) {
                is SearchItem.Songs -> if (newItem is SearchItem.Songs)
                    current.copy(songs = (current.songs + newItem.songs).distinctBy { it.trackId })
                else newItem

                is SearchItem.Albums -> if (newItem is SearchItem.Albums)
                    current.copy(albums = (current.albums + newItem.albums).distinctBy { it.collectionId })
                else newItem

                is SearchItem.Artists -> if (newItem is SearchItem.Artists)
                    current.copy(artists = (current.artists + newItem.artists).distinctBy { it.artistId })
                else newItem
            }
            Success(merged)
        }

        else -> Success(newItem)
    }

    @Parcelize
    data class SearchMusicsUiState(
        val userResearch: String = String(),
        val userResearchType: ResearchType = ResearchType.MUSICS,
        val researchStatus: ResearchStatus,
        val isPaging: Boolean = false,
        val isEndReached: Boolean = false,
        val isPagingError: Boolean = false,
    ) : Parcelable {

        enum class ResearchType { MUSICS, ALBUMS, ARTISTS, }

        @Parcelize
        sealed interface ResearchStatus : Parcelable {

            @Parcelize
            data class Success(val items: SearchItem) : ResearchStatus {

                @Parcelize
                sealed interface SearchItem : Parcelable {
                    @Parcelize
                    data class Songs(val songs: List<Song>) : SearchItem

                    @Parcelize
                    data class Albums(val albums: List<Album>) : SearchItem

                    @Parcelize
                    data class Artists(val artists: List<Artist>) : SearchItem
                }
            }

            @Parcelize
            data object Loading : ResearchStatus

            @Parcelize
            data object Error : ResearchStatus

            @Parcelize
            data object Idle : ResearchStatus
        }
    }
}

private const val SEARCH_MUSICS = "handle:Search"
