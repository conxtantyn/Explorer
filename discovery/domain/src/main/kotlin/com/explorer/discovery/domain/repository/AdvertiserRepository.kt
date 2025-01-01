package com.explorer.discovery.domain.repository

import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import kotlinx.coroutines.flow.SharedFlow

interface AdvertiserRepository {
    fun start(identifier: String, name: String)

    fun status(): SharedFlow<Status>

    fun devices(): List<Device>

    fun stop()
}
