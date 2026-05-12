package com.shubham.rctransmitter.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.rctransmitter.data.SerialPortDataSource
import com.shubham.rctransmitter.data.SettingsManager
import com.shubham.rctransmitter.domain.UDPController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val udpController: UDPController,
    private val serialPortDataSource: SerialPortDataSource,
    private val settingsManager: SettingsManager
) : ViewModel() {

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

    init {
        loadSettings()
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

    fun toggleReady() {
        viewModelScope.launch {
            _isReady.value = !_isReady.value
            val command = if (_isReady.value) "START" else "STOP"
            sendCommand(command)
        }
    }

    fun updateLeftStick(x: Float?, y: Float?) {
        x?.let { _leftStickX.value = it }
        y?.let { _leftStickY.value = it }

        viewModelScope.launch {
            sendCommand("LEFT:${_leftStickX.value},${_leftStickY.value}")
        }
    }

    fun updateRightStick(x: Float?, y: Float?) {
        x?.let { _rightStickX.value = it }
        y?.let { _rightStickY.value = it }

        viewModelScope.launch {
            sendCommand("RIGHT:$x,$y")
        }
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
