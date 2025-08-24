package com.infomaniak.searchmusic.injection

import com.infomaniak.searchmusic.usecases.GetAlbumsUseCase
import com.infomaniak.searchmusic.usecases.GetAlbumsUseCaseImpl
import com.infomaniak.searchmusic.usecases.GetArtistsUseCase
import com.infomaniak.searchmusic.usecases.GetArtistsUseCaseImpl
import com.infomaniak.searchmusic.usecases.GetSongsUseCase
import com.infomaniak.searchmusic.usecases.GetSongsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchMusicsModule {

    @Binds
    abstract fun bindGetSongsUseCase(
        impl: GetSongsUseCaseImpl
    ): GetSongsUseCase

    @Binds
    abstract fun bindGetArtistsUseCase(
        impl: GetArtistsUseCaseImpl
    ): GetArtistsUseCase

    @Binds
    abstract fun bindGetAlbumsUseCase(
        impl: GetAlbumsUseCaseImpl
    ): GetAlbumsUseCase
}