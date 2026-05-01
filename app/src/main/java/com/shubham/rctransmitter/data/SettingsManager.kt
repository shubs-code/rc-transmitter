package com.shubham.rctransmitter.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "rc_settings")

@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val UDP_IP_KEY = stringPreferencesKey("udp_ip")
        private val UDP_PORT_KEY = stringPreferencesKey("udp_port")
        private const val DEFAULT_IP = "192.168.1.100"
        private const val DEFAULT_PORT = "5000"
    }

    val udpIpFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[UDP_IP_KEY] ?: DEFAULT_IP
    }

    val udpPortFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[UDP_PORT_KEY] ?: DEFAULT_PORT
    }

    suspend fun setUdpIp(ip: String) {
        context.dataStore.edit { preferences ->
            preferences[UDP_IP_KEY] = ip
        }
    }

    suspend fun setUdpPort(port: String) {
        context.dataStore.edit { preferences ->
            preferences[UDP_PORT_KEY] = port
        }
    }

    suspend fun getUdpIp(): String {
        return context.dataStore.data.map { preferences ->
            preferences[UDP_IP_KEY] ?: DEFAULT_IP
        }.first()
    }

    suspend fun getUdpPort(): String {
        return context.dataStore.data.map { preferences ->
            preferences[UDP_PORT_KEY] ?: DEFAULT_PORT
        }.first()
    }
}
