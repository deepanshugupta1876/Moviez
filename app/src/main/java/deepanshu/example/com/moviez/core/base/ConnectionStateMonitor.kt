package deepanshu.example.com.moviez.core.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import deepanshu.example.com.moviez.utils.AppConstants


class ConnectionStateMonitor(private val mContext: Context) : LiveData<Boolean>() {
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var networkReceiver: NetworkReceiver
    private val connectivityManager: ConnectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = NetworkCallback(this)
        } else {
            networkReceiver = NetworkReceiver()
        }
    }

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val networkRequest = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            }
            else -> {
                mContext.registerReceiver(
                    networkReceiver,
                    IntentFilter(AppConstants.CONNECTIVITY_ACTION))
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            mContext.unregisterReceiver(networkReceiver)
        }
    }

    fun callInActive() {
        onInactive()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    internal inner class NetworkCallback(private val mConnectionStateMonitor: ConnectionStateMonitor) :
        ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            mConnectionStateMonitor.postValue(true)
        }

        override fun onLost(network: Network) {
            mConnectionStateMonitor.postValue(false)
        }

    }

    private fun updateConnection() {
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            postValue(true)
        } else {
            postValue(false)
        }
    }

    internal inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == AppConstants.CONNECTIVITY_ACTION) {
                updateConnection()
            }
        }
    }
}