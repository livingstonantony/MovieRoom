package com.ell.movieroom.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ell.movieroom.data.MetaDataReader
import com.ell.movieroom.data.MetaDataReaderImpl
import com.ell.movieroom.datastore.DataStoreViewModel
import com.ell.movieroom.datastore.UserPreferencesRepository
import com.ell.movieroom.presentation.devices.DeviceViewModel
import com.ell.movieroom.viewmodel.MainViewModel

class AppContainer(private val app: Application) {

    private val sharedAppContainer = SharedAppContainer()
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

    val deviceViewModelFactory = viewModelFactory {
        initializer {
            DeviceViewModel(
                deviceApiService = sharedAppContainer.deviceApiService,
                service = sharedAppContainer.service
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
    val dataStoreViewModel = viewModelFactory {
        initializer {

            DataStoreViewModel(
                userPreferencesRepository = userPreferencesRepository
            )
        }
    }

    lateinit var dataStore: DataStore<Preferences>
        private set // Prevents modification from outside

    // A lazy-initialized repository that uses the dataStore
    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(dataStore)
    }

    // An initialization function to be called from platform-specific code
    fun init(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
    }

}
