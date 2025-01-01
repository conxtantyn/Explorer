package com.explorer.discovery.data.datasource

import com.explorer.discovery.domain.model.Device

interface ConsumerDatasource {
    fun start(identifier: String)

    fun stop()

    interface Listener {
        fun onDiscover(device: Device)

        fun onError(code: Int)
    }
}
