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

    val commMode: StateFlow<String> = settingsManager.commModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = "UDP"
    )

    val leftMinX: StateFlow<Int> = settingsManager.leftMinXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = -100
    )

    val leftMaxX: StateFlow<Int> = settingsManager.leftMaxXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = 100
    )

    val leftMinY: StateFlow<Int> = settingsManager.leftMinYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = -100
    )

    val leftMaxY: StateFlow<Int> = settingsManager.leftMaxYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = 100
    )

    val rightMinX: StateFlow<Int> = settingsManager.rightMinXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = -100
    )

    val rightMaxX: StateFlow<Int> = settingsManager.rightMaxXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = 100
    )

    val rightMinY: StateFlow<Int> = settingsManager.rightMinYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = -100
    )

    val rightMaxY: StateFlow<Int> = settingsManager.rightMaxYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = 100
    )

    val leftXSnapBack: StateFlow<Boolean> = settingsManager.leftXSnapBackFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = true // default snap
    )
    val leftXSnapPercent: StateFlow<Int> = settingsManager.leftXSnapPercentFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = 50 // default center
    )

    val leftYSnapBack: StateFlow<Boolean> = settingsManager.leftYSnapBackFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = false // default throttle (sticky)
    )
    val leftYSnapPercent: StateFlow<Int> = settingsManager.leftYSnapPercentFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = 100 // default bottom/top depending on setup
    )

    val rightXSnapBack: StateFlow<Boolean> = settingsManager.rightXSnapBackFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = true // default snap
    )
    val rightXSnapPercent: StateFlow<Int> = settingsManager.rightXSnapPercentFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = 50 // default center
    )

    val rightYSnapBack: StateFlow<Boolean> = settingsManager.rightYSnapBackFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = false // default throttle (sticky)
    )
    val rightYSnapPercent: StateFlow<Int> = settingsManager.rightYSnapPercentFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = 100 // default bottom/top depending on setup
    )

    fun saveSettings(
        ip: String,
        port: String,
        mode: String,
        leftMinX: Int,
        leftMaxX: Int,
        leftMinY: Int,
        leftMaxY: Int,
        rightMinX: Int,
        rightMaxX: Int,
        rightMinY: Int,
        rightMaxY: Int,
        leftXSnapBack: Boolean, leftXSnapPercent: Int,
        leftYSnapBack: Boolean, leftYSnapPercent: Int,
        rightXSnapBack: Boolean, rightXSnapPercent: Int,
        rightYSnapBack: Boolean, rightYSnapPercent: Int
    ) {
        viewModelScope.launch {
            settingsManager.setUdpIp(ip)
            settingsManager.setUdpPort(port)
            settingsManager.setCommMode(mode)
            settingsManager.setLeftMinX(leftMinX)
            settingsManager.setLeftMaxX(leftMaxX)
            settingsManager.setLeftMinY(leftMinY)
            settingsManager.setLeftMaxY(leftMaxY)
            settingsManager.setRightMinX(rightMinX)
            settingsManager.setRightMaxX(rightMaxX)
            settingsManager.setRightMinY(rightMinY)
            settingsManager.setRightMaxY(rightMaxY)
            settingsManager.setLeftXSnapBack(leftXSnapBack)
            settingsManager.setLeftXSnapPercent(leftXSnapPercent)
            settingsManager.setLeftYSnapBack(leftYSnapBack)
            settingsManager.setLeftYSnapPercent(leftYSnapPercent)
            settingsManager.setRightXSnapBack(rightXSnapBack)
            settingsManager.setRightXSnapPercent(rightXSnapPercent)
            settingsManager.setRightYSnapBack(rightYSnapBack)
            settingsManager.setRightYSnapPercent(rightYSnapPercent)
        }
    }
}