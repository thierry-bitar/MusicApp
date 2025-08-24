package com.infomaniak.musicrepository.injection

import com.infomaniak.musicrepository.MusicRepositoryImpl
import com.infomaniak.searchmusic.repositories.GetAlbumsRepository
import com.infomaniak.searchmusic.repositories.GetArtistsRepository
import com.infomaniak.searchmusic.repositories.GetSongsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MusicRepositoryModule {

    @Binds
    abstract fun bindGetSongsRepository(
        repository: MusicRepositoryImpl
    ): GetSongsRepository

    @Binds
    abstract fun bindGetArtistsRepository(
        repository: MusicRepositoryImpl
    ): GetArtistsRepository

    @Binds
    abstract fun bindGetAlbumsRepository(
        repository: MusicRepositoryImpl
    ): GetAlbumsRepository
}
