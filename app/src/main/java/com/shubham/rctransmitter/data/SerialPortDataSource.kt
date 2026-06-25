package com.shubham.rctransmitter.data

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.shubham.rctransmitter.receivers.UsbPermissionReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SerialPortDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    companion object {
        private const val TAG = "SerialPort"
        private const val ACTION_USB_PERMISSION = "com.shubham.rctransmitter.USB_PERMISSION"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var serialPort: UsbSerialPort? = null
    private var usbPermissionReceiver: BroadcastReceiver? = null
    private var isConnected = false
    private var pendingDevice: UsbDevice? = null

    private val _serialDataFlow = MutableSharedFlow<ByteArray>(replay = 0)
    val serialDataFlow: SharedFlow<ByteArray> = _serialDataFlow.asSharedFlow()

    init {
        registerUsbPermissionReceiver()
    }

    private fun registerUsbPermissionReceiver() {
        val intentFilter = IntentFilter(ACTION_USB_PERMISSION)

        usbPermissionReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context == null || intent == null) return

                val action = intent.action
                Log.d(TAG, "Broadcast action: $action")

                if (action == ACTION_USB_PERMISSION) {
                    val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                    val permissionGranted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)

                    Log.d(TAG, "Permission broadcast received")
                    Log.d(TAG, "Device: $device")
                    Log.d(TAG, "Permission granted: $permissionGranted")

                    if (permissionGranted && device != null) {
                        Log.d(TAG, "✅ USB PERMISSION GRANTED for ${device.deviceName}")
                        scope.launch {
                            connectToDevice(device)
                        }
                    } else {
                        Log.e(TAG, "❌ USB PERMISSION DENIED")
                        pendingDevice = null
                    }
                }
            }
        }

        try {
            context.registerReceiver(usbPermissionReceiver, intentFilter, Context.RECEIVER_EXPORTED)
            Log.d(TAG, "✅ USB permission receiver registered")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register USB receiver: ${e.message}", e)
        }
    }

    fun connectToUSB() {
        scope.launch {
            try {
                Log.d(TAG, "=== Starting USB Connection ===")

                disconnect()

                val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

                val allDevices = usbManager.deviceList
                Log.d(TAG, "Total USB devices: ${allDevices.size}")

                if (allDevices.isEmpty()) {
                    Log.e(TAG, "No USB devices found")
                    return@launch
                }

                allDevices.forEach { (name, device) ->
                    Log.d(TAG, "Found device: $name - ${device.deviceName}")
                }

                val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
                Log.d(TAG, "Found ${drivers.size} compatible USB serial drivers")

                if (drivers.isEmpty()) {
                    Log.e(TAG, "No compatible USB serial drivers found")
                    return@launch
                }

                val driver = drivers.first()
                val device = driver.device

                Log.d(TAG, "Selected device: ${device.deviceName}")
                Log.d(TAG, "VendorID: 0x${device.vendorId.toString(16)}")
                Log.d(TAG, "ProductID: 0x${device.productId.toString(16)}")

                val hasPermission = usbManager.hasPermission(device)
                Log.d(TAG, "Current permission status: $hasPermission")

                if (!hasPermission) {
                    Log.d(TAG, "Requesting USB permission...")
                    pendingDevice = device
                    requestUsbPermission(device)
                    return@launch
                }

                connectToDevice(device)

            } catch (e: Exception) {
                Log.e(TAG, "Connection error: ${e.message}", e)
            }
        }
    }

    private fun requestUsbPermission(device: UsbDevice) {
        try {
            val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

            val intent = Intent(context, UsbPermissionReceiver::class.java).apply {
                action = ACTION_USB_PERMISSION
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            Log.d(TAG, "Requesting permission for: ${device.deviceName}")
            usbManager.requestPermission(device, pendingIntent)
            Log.d(TAG, "Permission request sent")

        } catch (e: Exception) {
            Log.e(TAG, "Error requesting permission: ${e.message}", e)
        }
    }

    private suspend fun connectToDevice(device: UsbDevice) {
        try {
            Log.d(TAG, "Opening USB connection...")

            val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

            val connection = usbManager.openDevice(device)
            if (connection == null) {
                Log.e(TAG, "Failed to open USB connection")
                return
            }

            Log.d(TAG, "✅ USB connection opened")

            val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
            if (drivers.isEmpty()) {
                Log.e(TAG, "No drivers found")
                connection.close()
                return
            }

            val driver = drivers.first()
            if (driver.ports.isEmpty()) {
                Log.e(TAG, "No ports available")
                connection.close()
                return
            }

            val port = driver.ports.first()

            Log.d(TAG, "Configuring serial port at 115200 baud...")
            port.open(connection)
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)

            serialPort = port
            isConnected = true
            pendingDevice = null

            Log.d(TAG, "✅✅✅ SUCCESSFULLY CONNECTED TO USB DEVICE ✅✅✅")
            startReading()
        } catch (e: Exception) {
            Log.e(TAG, "Connection error: ${e.message}", e)
        }
    }

    private fun startReading() {
        scope.launch {
            val buffer = ByteArray(4096)

            while (isConnected && serialPort != null) {
                try {
                    val count = serialPort?.read(buffer, 1000) ?: 0
                    if (count > 0) {
                        val data = buffer.copyOfRange(0, count)
                        Log.d(TAG, "📥 Received ${count} bytes")
                        _serialDataFlow.emit(data)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Read error: ${e.message}", e)
                    break
                }
            }
        }
    }



    fun sendData(data: String) {
        scope.launch {
            try {
                if (isConnected && serialPort != null) {
                    val bytes = (data + "\n").toByteArray()
                    serialPort?.write(bytes, 1000)
                } else {
                    connectToUSB()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Write error: ${e.message}", e)
            }
        }
    }

    fun disconnect() {
        try {
            serialPort?.close()
            serialPort = null
            isConnected = false
            pendingDevice = null

            Log.d(TAG, "Disconnected")

        } catch (e: Exception) {
            Log.e(TAG, "Disconnect error: ${e.message}", e)
        }
    }

    fun isUSBConnected(): Boolean = isConnected

    fun release() {
        disconnect()

        try {
            if (usbPermissionReceiver != null) {
                context.unregisterReceiver(usbPermissionReceiver)
                Log.d(TAG, "Receiver unregistered")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering receiver: ${e.message}", e)
        }

        scope.cancel()
    }
}