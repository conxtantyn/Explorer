package com.explorer.discovery.local.datasource

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import com.explorer.discovery.data.datasource.ConsumerDatasource
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.local.factory.ConsumerFactory
import com.explorer.discovery.local.BuildConfig
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressLint("MissingPermission")
class ConsumerDatasourceDelegate @Inject constructor(
    private val factory: ConsumerFactory,
    private val manager: BluetoothManager,
    private val listener: ConsumerDatasource.Listener
) : ScanCallback(), ConsumerDatasource {
    @Volatile private var started: Boolean = false

    override fun start(identifier: String) {
        if (started) {
            return
        }
        val scanSettings = factory.settings().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
        val filters = listOf(
            factory.filter()
                .setServiceUuid(ParcelUuid(UUID.fromString(identifier)))
                .build()
        )
        manager.adapter.bluetoothLeScanner.startScan(filters, scanSettings, this)
        started = true
    }

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        result?.let { handleResult(it) }
    }

    private fun handleResult(result: ScanResult) {
        val name = result.scanRecord?.getManufacturerSpecificData(BuildConfig.MANUFACTURER) ?: return
        val device = Device(
            name = String(name, Charsets.UTF_8),
            address = result.device.address
        )
        listener.onDiscover(device)
    }

    override fun onScanFailed(errorCode: Int) {
        started = false
        listener.onError(errorCode)
    }

    override fun stop() {
        started = false
        manager.adapter.bluetoothLeScanner.stopScan(this)
    }
}
