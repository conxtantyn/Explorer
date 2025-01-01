package com.explorer.discovery.data.datasource

import com.explorer.discovery.domain.model.Device

interface AdvertiserDatasource {
    fun start(identifier: String, name: String)

    fun devices(): List<Device>

    fun stop()

    interface Listener {
        fun onStart()

        fun onError(code: Int)
    }
}
