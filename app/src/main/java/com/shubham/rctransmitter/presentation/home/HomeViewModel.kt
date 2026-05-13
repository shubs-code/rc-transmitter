package com.shubham.rctransmitter.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.rctransmitter.data.SerialPortDataSource
import com.shubham.rctransmitter.data.SettingsManager
import com.shubham.rctransmitter.domain.UDPController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val udpController: UDPController,
    private val serialPortDataSource: SerialPortDataSource,
    private val settingsManager: SettingsManager
) : ViewModel() {

    companion object {
        private const val SEND_INTERVAL_MS = 100L
    }

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _leftStickX = MutableStateFlow(0f)
    val leftStickX: StateFlow<Float> = _leftStickX.asStateFlow()

    private val _leftStickY = MutableStateFlow(1f)
    val leftStickY: StateFlow<Float> = _leftStickY.asStateFlow()

    private val _rightStickX = MutableStateFlow(0f)
    val rightStickX: StateFlow<Float> = _rightStickX.asStateFlow()

    private val _rightStickY = MutableStateFlow(0f)
    val rightStickY: StateFlow<Float> = _rightStickY.asStateFlow()

    val commMode: StateFlow<String> = settingsManager.commModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "UDP"
    )

    private val leftMinX: StateFlow<Int> = settingsManager.leftMinXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = -100
    )

    private val leftMaxX: StateFlow<Int> = settingsManager.leftMaxXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 100
    )

    private val leftMinY: StateFlow<Int> = settingsManager.leftMinYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = -100
    )

    private val leftMaxY: StateFlow<Int> = settingsManager.leftMaxYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 100
    )

    private val rightMinX: StateFlow<Int> = settingsManager.rightMinXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = -100
    )

    private val rightMaxX: StateFlow<Int> = settingsManager.rightMaxXFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 100
    )

    private val rightMinY: StateFlow<Int> = settingsManager.rightMinYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = -100
    )

    private val rightMaxY: StateFlow<Int> = settingsManager.rightMaxYFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 100
    )

    init {
        loadSettings()
        startPeriodicSender()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                settingsManager.udpIpFlow,
                settingsManager.udpPortFlow,
                settingsManager.commModeFlow
            ) { ip, port, mode ->
                Triple(ip, port, mode)
            }.collect { (ip, port, mode) ->
                if (mode == "UDP") {
                    udpController.setTarget(ip, port.toIntOrNull() ?: 5000)
                } else {
                    serialPortDataSource.connectToUSB()
                }
            }
        }
    }

    private fun startPeriodicSender() {
        viewModelScope.launch {
            while (isActive) {
                try {
                    val scaledLeftX = mapRange(_leftStickX.value, leftMinX.value, leftMaxX.value)
                    val scaledLeftY = mapRange(-_leftStickY.value, leftMinY.value, leftMaxY.value)
                    val scaledRightX = mapRange(_rightStickX.value, rightMinX.value, rightMaxX.value)
                    val scaledRightY = mapRange(-_rightStickY.value, rightMinY.value, rightMaxY.value)
                    val command = "$scaledLeftX,$scaledLeftY,$scaledRightX,$scaledRightY\n"
                    sendCommand(command)
                    delay(SEND_INTERVAL_MS)
                } catch (e: Exception) {
                    e.printStackTrace()
                    delay(SEND_INTERVAL_MS)
                }
            }
        }
    }

    fun toggleReady() {
        viewModelScope.launch {
            _isReady.value = !_isReady.value
            val command = if (_isReady.value) "START" else "STOP"
//            sendCommand(command)
        }
    }

    fun updateLeftStick(x: Float?, y: Float?) {
        if(x!=null)_leftStickX.value = x
        if(y!=null)_leftStickY.value = y
    }

    fun updateRightStick(x: Float?, y: Float?) {
        if(x!=null)_rightStickX.value = x
        if(y!=null)_rightStickY.value = y
    }

    private fun mapRange(value: Float, minValue: Int, maxValue: Int): Int {
        return (value * (maxValue - minValue) / 2 + (minValue + maxValue) / 2).toInt()
    }

    private suspend fun sendCommand(command: String) {
        val mode = settingsManager.getCommMode()
        if (mode == "UDP") {
            udpController.sendCommand(command)
        } else {
            serialPortDataSource.sendData(command)
        }
    }

    override fun onCleared() {
        super.onCleared()
        serialPortDataSource.disconnect()
    }
}