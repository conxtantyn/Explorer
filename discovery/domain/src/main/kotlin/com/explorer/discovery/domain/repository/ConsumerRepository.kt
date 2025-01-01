package com.explorer.discovery.domain.repository

import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import kotlinx.coroutines.flow.SharedFlow

interface ConsumerRepository {
    fun start(identifier: String)

    fun status(): SharedFlow<Status>

    fun devices(): SharedFlow<List<Device>>

    fun stop()
}
