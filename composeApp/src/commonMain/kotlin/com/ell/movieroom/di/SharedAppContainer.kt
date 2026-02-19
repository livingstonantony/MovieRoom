package com.ell.movieroom.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ell.movieroom.data.remote.DeviceApiService
import com.ell.movieroom.data.remote.DeviceSocketService
import com.ell.movieroom.data.remote.createHttpClient
import com.ell.movieroom.presentation.devices.DeviceViewModel
import io.ktor.client.HttpClient

class SharedAppContainer {


    val service: DeviceSocketService by lazy {
        DeviceSocketService(client = client)
    }

    val deviceApiService: DeviceApiService by lazy {
        DeviceApiService(client = client)
    }

    private val client: HttpClient by lazy {
        createHttpClient()
    }

    val deviceViewModel = viewModelFactory {
        initializer {
            DeviceViewModel(
                deviceApiService,
                service
            )

        }
    }
}