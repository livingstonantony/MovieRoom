package com.ell.movieroom.di

import com.ell.movieroom.data.remote.createHttpClient
import com.ell.movieroom.presentation.devices.DeviceViewModel
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient() } // Your Ktor setup
    single { DeviceViewModel(get()) }

    // Use factory for ViewModels to get a fresh instance per screen
    factory { DeviceViewModel(get()) }
}
