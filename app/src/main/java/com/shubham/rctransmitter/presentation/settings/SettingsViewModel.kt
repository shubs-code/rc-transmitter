package com.shubham.rctransmitter.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.rctransmitter.data.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val udpIp: StateFlow<String> = settingsManager.udpIpFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = "192.168.1.100"
    )

    val udpPort: StateFlow<String> = settingsManager.udpPortFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = "5000"
    )

    fun saveSettings(ip: String, port: String) {
        viewModelScope.launch {
            settingsManager.setUdpIp(ip)
            settingsManager.setUdpPort(port)
        }
    }
}
