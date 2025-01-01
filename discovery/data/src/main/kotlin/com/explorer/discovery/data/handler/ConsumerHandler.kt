package com.explorer.discovery.data.handler

import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import kotlinx.coroutines.flow.SharedFlow

interface ConsumerHandler {
    fun status(): SharedFlow<Status>

    fun onStart()

    fun devices(): SharedFlow<List<Device>>

    fun reset()
}
