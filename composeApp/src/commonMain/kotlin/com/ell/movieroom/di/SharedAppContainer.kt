package com.ell.movieroom.di

import com.ell.movieroom.data.remote.DeviceApiService
import com.ell.movieroom.data.remote.DeviceSocketService
import com.ell.movieroom.data.remote.createHttpClient
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

}