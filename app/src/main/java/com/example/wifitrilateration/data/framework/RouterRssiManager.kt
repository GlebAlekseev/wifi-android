package com.example.wifitrilateration.data.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import com.example.wifitrilateration.data.framework.entity.RouterRssi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

// Класс отвечает за получение RSSI от WiFiManager
class RouterRssiManager @Inject constructor(private val context: Context) {
    private val scanResultsChannel = ConflatedBroadcastChannel<List<RouterRssi>>()
    private lateinit var wifiManager: WifiManager

    fun getRouterRssiList(): Flow<List<RouterRssi>> {
        wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.startScan()
        context.registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        return scanResultsChannel.asFlow()
    }

    fun unregisterReceiver(){
        try {
            context.unregisterReceiver(wifiScanReceiver)
        }catch (e: Exception){}
    }

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val scanResults = wifiManager.scanResults
            scanResults.sortBy { it.level }
            scanResultsChannel.trySend(scanResults.map { RouterRssi(it.BSSID,it.level.toDouble()) })
            CoroutineScope(Dispatchers.Default).launch {
                delay(30 * 1000)
                wifiManager.startScan()
            }
        }
    }
}