package com.ell.movieroom.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val savedUri = userPreferencesRepository.savedUriFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onNewUriSelected(uriString: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUri(uriString)
        }
    }
}
