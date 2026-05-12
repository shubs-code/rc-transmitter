package com.shubham.rctransmitter.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
                Log.d(TAG, "📱 USB Device Attached")
            }
            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                Log.d(TAG, "📱 USB Device Detached")
            }
        }
    }
}
