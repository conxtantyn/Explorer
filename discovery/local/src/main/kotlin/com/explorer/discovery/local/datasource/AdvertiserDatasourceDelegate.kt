package com.explorer.discovery.local.datasource

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.os.ParcelUuid
import com.explorer.discovery.data.datasource.AdvertiserDatasource
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.local.factory.AdvertiserFactory
import com.explorer.discovery.local.mapper.mapToDomain
import com.explorer.discovery.local.BuildConfig
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressLint("MissingPermission")
class AdvertiserDatasourceDelegate @Inject constructor(
    private val manager: BluetoothManager,
    private val factory: AdvertiserFactory,
    private val listener: AdvertiserDatasource.Listener
) : AdvertiseCallback(), AdvertiserDatasource {
    @Volatile private var started: Boolean = false

    override fun start(identifier: String, name: String) {
        if (started) {
            return
        }
        startAdvertising(identifier, name)
    }

    private fun startAdvertising(identifier: String, name: String) {
        val settings = factory.settings()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(BuildConfig.CONNECTABLE)
            .setTimeout(TIMEOUT)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .build()
        val data = factory.data()
            .setIncludeDeviceName(INCLUDE_DEVICE_NAME)
            .addServiceUuid(ParcelUuid(UUID.fromString(identifier)))
            .addManufacturerData(BuildConfig.MANUFACTURER, name.toByteArray(Charsets.UTF_8))
            .build()
        manager.adapter.bluetoothLeAdvertiser.startAdvertising(settings,  data, this)
    }

    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
        started = true
        listener.onStart()
    }

    override fun devices(): List<Device> {
        val devices = mutableListOf<Device>()
        devices.addAll(manager.adapter.bondedDevices.map(BluetoothDevice::mapToDomain))
        val gattDevices = manager.getConnectedDevices(BluetoothProfile.GATT)
        devices.addAll(gattDevices.map(BluetoothDevice::mapToDomain))
        return devices
    }

    override fun onStartFailure(errorCode: Int) {
        started = false
        listener.onError(errorCode)
    }

    override fun stop() {
        manager.adapter.bluetoothLeAdvertiser.stopAdvertising(this)
        started = false
    }

    internal companion object {
        const val TIMEOUT = 0
        const val INCLUDE_DEVICE_NAME = false
    }
}