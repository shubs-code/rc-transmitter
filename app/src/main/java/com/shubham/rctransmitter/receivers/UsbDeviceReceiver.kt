package com.shubham.rctransmitter.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log

class UsbDeviceReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "UsbDeviceReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val action = intent.action
        Log.d(TAG, "USB action received: $action")

        when (action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                Log.d(TAG, "📱 USB Device Attached: ${device?.deviceName}")

                // Launch main activity when USB device is attached
                launchApp(context)
            }
            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                Log.d(TAG, "📱 USB Device Detached: ${device?.deviceName}")
            }
        }
    }

    private fun launchApp(context: Context) {
        try {
            val intent = Intent(context, Class.forName("com.shubham.rctransmitter.presentation.MainActivity"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = UsbManager.ACTION_USB_DEVICE_ATTACHED
            context.startActivity(intent)
            Log.d(TAG, "App launched")
        } catch (e: Exception) {
            Log.e(TAG, "Error launching app: ${e.message}", e)
        }
    }
}