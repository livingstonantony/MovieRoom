package com.ell.movieroom.presentation.devices


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ell.movieroom.data.remote.DeviceSocketService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class DeviceViewModel(
    private val service: DeviceSocketService
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceState())
    val state: StateFlow<DeviceState> = _state.asStateFlow()

    private var socketJob: Job? = null

    fun onIntent(intent: DeviceIntent) {
        when (intent) {
            DeviceIntent.Connect -> startSocket()
            DeviceIntent.Disconnect -> stopSocket()
        }
    }

    private fun startSocket() {
        if (socketJob != null) return // prevent multiple connections

        socketJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            service.observeDevices()
                .onEach { response ->
                    _state.update {
                        it.copy(
                            devices = response.devices,
                            isConnected = true,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .catch { e ->
                    println("ERROR: ${e.message}")
                    _state.update {
                        it.copy(
                            error = e.message,
                            isConnected = false,
                            isLoading = false
                        )
                    }
                    socketJob = null
                }
                .collect()
        }
    }

    private fun stopSocket() {
        socketJob?.cancel()
        socketJob = null
        _state.update { it.copy(isConnected = false) }
    }

    override fun onCleared() {
        stopSocket()
        super.onCleared()
    }
}

