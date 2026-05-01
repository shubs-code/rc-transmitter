package com.shubham.rctransmitter.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _leftStickX = MutableStateFlow(0f)
    val leftStickX: StateFlow<Float> = _leftStickX.asStateFlow()

    private val _leftStickY = MutableStateFlow(0f)
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
                settingsManager.udpPortFlow
            ) { ip, port ->
                Pair(ip, port)
            }.collect { (ip, port) ->
                udpController.setTarget(ip, port.toIntOrNull() ?: 5000)
            }
        }
    }

    fun toggleReady() {
        viewModelScope.launch {
            _isReady.value = !_isReady.value
            val command = if (_isReady.value) "START" else "STOP"
            udpController.sendCommand(command)
        }
    }

    fun updateLeftStick(x: Float, y: Float) {
        _leftStickX.value = x
        _leftStickY.value = y
        viewModelScope.launch {
            udpController.sendCommand("LEFT:$x,$y")
        }
    }

    fun updateRightStick(x: Float, y: Float) {
        _rightStickX.value = x
        _rightStickY.value = y
        viewModelScope.launch {
            udpController.sendCommand("RIGHT:$x,$y")
        }
    }
}
