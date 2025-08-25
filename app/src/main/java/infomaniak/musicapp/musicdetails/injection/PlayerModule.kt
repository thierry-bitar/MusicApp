package infomaniak.musicapp.musicdetails.injection

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import infomaniak.musicapp.musicdetails.MusicPlayerControllerImpl
import infomaniak.musicapp.musicdetails.MusicPlayerController

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlayerModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPlayerController(
        impl: MusicPlayerControllerImpl
    ): MusicPlayerController

    companion object {
        @Provides
        @ViewModelScoped
        fun provideExoPlayer(
            @ApplicationContext context: Context
        ): ExoPlayer = ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }
}