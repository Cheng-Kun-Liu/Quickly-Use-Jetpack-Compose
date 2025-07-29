package com.laomuji1999.compose.core.logic.network.http

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.logic.common.dispatchers.IoCoroutineScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 当前网络状态的观察者
 * 必须要有 ACCESS_NETWORK_STATE 权限
 * 获取网络状态: [isConnected]
 * 监听网络状态: [isConnectedFlow]
 */
@Singleton
class ConnectivityObserver @Inject constructor(
    @ApplicationContext context: Context,
    @IoCoroutineScope coroutineScope: CoroutineScope
) {
    companion object {
        /**
         * 网络有效验证.
         * 因为存在内网的情况,所以默认不开启,只要用户开启了网络就认为有网络.
         */
        const val VALIDATED = false

        const val TAG = "tag_ConnectivityObserver"
    }

    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!
    private val _isConnected: Flow<Boolean>
        get() = callbackFlow {
            val callback = object : NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (VALIDATED) {
                        val connected =
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                        Log.debug(TAG, "onCapabilitiesChanged: $connected")
                        trySend(connected)
                    } else {
                        trySend(true)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.debug(TAG, "onUnavailable")
                    trySend(false)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.debug(TAG, "onLost")
                    trySend(false)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.debug(TAG, "onAvailable")
                    trySend(true)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()

    var isConnected = false
        private set

    private val _isConnectedFlow = MutableStateFlow(isConnected)
    val isConnectedFlow: Flow<Boolean> = _isConnectedFlow

    init {
        coroutineScope.launch {
            _isConnected.collect {
                if (isConnected != it) {
                    isConnected = it
                    _isConnectedFlow.value = it
                }
            }
        }
    }
}