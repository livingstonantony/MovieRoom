package com.ell.movieroom.di

import android.app.Application
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ell.movieroom.viewmodel.MainViewModel
import com.ell.movieroom.data.MetaDataReader
import com.ell.movieroom.data.MetaDataReaderImpl

class AppContainer(private val app: Application) {

    val mainViewModelFactory = viewModelFactory {
        initializer {
            val savedStateHandle = this.createSavedStateHandle()

            MainViewModel(
                savedStateHandle = savedStateHandle,
                player = player,
                metaDataReader = metaDataReader
            )
        }
    }
    private val player: Player by lazy {
        ExoPlayer.Builder(app)
            .build()
    }

    private val metaDataReader: MetaDataReader by lazy {
        MetaDataReaderImpl(app)
    }

}