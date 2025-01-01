package com.explorer.discovery.local.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.explorer.discovery.domain.model.Device

@SuppressLint("MissingPermission")
fun BluetoothDevice.mapToDomain(): Device {
    return Device(
        name = name,
        address = address
    )
}
