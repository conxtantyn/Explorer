package com.explorer.discovery.ui.mapper

import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.ui.model.UiDevice

fun Device.mapToUi(): UiDevice {
    return UiDevice(
        name = name,
        address = address
    )
}
