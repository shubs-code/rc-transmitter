package com.shubham.rctransmitter.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.util.Log

class UsbPermissionReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "UsbPermissionReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val action = intent.action
        if (action == "com.shubham.rctransmitter.USB_PERMISSION") {
            val permissionGranted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
            Log.d(TAG, "USB Permission granted: $permissionGranted")
        }
    }
}
