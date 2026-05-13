package com.shubham.rctransmitter.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
        private val COMM_MODE_KEY = stringPreferencesKey("comm_mode")

        private val LEFT_MIN_X_KEY = intPreferencesKey("left_min_x")
        private val LEFT_MAX_X_KEY = intPreferencesKey("left_max_x")
        private val LEFT_MIN_Y_KEY = intPreferencesKey("left_min_y")
        private val LEFT_MAX_Y_KEY = intPreferencesKey("left_max_y")

        private val RIGHT_MIN_X_KEY = intPreferencesKey("right_min_x")
        private val RIGHT_MAX_X_KEY = intPreferencesKey("right_max_x")
        private val RIGHT_MIN_Y_KEY = intPreferencesKey("right_min_y")
        private val RIGHT_MAX_Y_KEY = intPreferencesKey("right_max_y")

        private const val DEFAULT_IP = "192.168.1.100"
        private const val DEFAULT_PORT = "5000"
        private const val DEFAULT_MODE = "UDP"
        private const val DEFAULT_MIN = -100
        private const val DEFAULT_MAX = 100
    }

    val udpIpFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[UDP_IP_KEY] ?: DEFAULT_IP
    }

    val udpPortFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[UDP_PORT_KEY] ?: DEFAULT_PORT
    }

    val commModeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[COMM_MODE_KEY] ?: DEFAULT_MODE
    }

    val leftMinXFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[LEFT_MIN_X_KEY] ?: DEFAULT_MIN
    }

    val leftMaxXFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[LEFT_MAX_X_KEY] ?: DEFAULT_MAX
    }

    val leftMinYFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[LEFT_MIN_Y_KEY] ?: DEFAULT_MIN
    }

    val leftMaxYFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[LEFT_MAX_Y_KEY] ?: DEFAULT_MAX
    }

    val rightMinXFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[RIGHT_MIN_X_KEY] ?: DEFAULT_MIN
    }

    val rightMaxXFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[RIGHT_MAX_X_KEY] ?: DEFAULT_MAX
    }

    val rightMinYFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[RIGHT_MIN_Y_KEY] ?: DEFAULT_MIN
    }

    val rightMaxYFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[RIGHT_MAX_Y_KEY] ?: DEFAULT_MAX
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

    suspend fun setCommMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[COMM_MODE_KEY] = mode
        }
    }

    suspend fun setLeftMinX(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[LEFT_MIN_X_KEY] = value
        }
    }

    suspend fun setLeftMaxX(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[LEFT_MAX_X_KEY] = value
        }
    }

    suspend fun setLeftMinY(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[LEFT_MIN_Y_KEY] = value
        }
    }

    suspend fun setLeftMaxY(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[LEFT_MAX_Y_KEY] = value
        }
    }

    suspend fun setRightMinX(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[RIGHT_MIN_X_KEY] = value
        }
    }

    suspend fun setRightMaxX(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[RIGHT_MAX_X_KEY] = value
        }
    }

    suspend fun setRightMinY(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[RIGHT_MIN_Y_KEY] = value
        }
    }

    suspend fun setRightMaxY(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[RIGHT_MAX_Y_KEY] = value
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

    suspend fun getCommMode(): String {
        return context.dataStore.data.map { preferences ->
            preferences[COMM_MODE_KEY] ?: DEFAULT_MODE
        }.first()
    }
}