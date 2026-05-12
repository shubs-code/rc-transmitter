package com.shubham.rctransmitter.domain

import android.util.Log
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UDPController @Inject constructor() {

    private var socket: DatagramSocket? = null
    private var address: InetAddress? = null
    private var currentIp: String = ""
    private var currentPort: Int = 5000

    suspend fun setTarget(ip: String, port: Int) {
        currentIp = ip
        currentPort = port

        address = withContext(Dispatchers.IO) {
            InetAddress.getByName(ip)
        }
        if (socket == null || socket?.isClosed == true) {
            socket = DatagramSocket()
        }
    }
    suspend fun sendCommand(command: String) {
        withContext(Dispatchers.IO) {
            try {
                val data = command.toByteArray()
                val packet = DatagramPacket(data, data.size, address, currentPort)
                socket?.send(packet)
                Log.d("UDPController", "Sent: $command to $currentIp:$currentPort")
            } catch (e: Exception) {
                Log.e("UDPController", "Error sending UDP: ${e.message}")
            }
        }
    }
}
