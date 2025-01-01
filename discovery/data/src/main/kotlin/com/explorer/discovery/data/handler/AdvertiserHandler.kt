package com.explorer.discovery.data.handler

import com.explorer.discovery.domain.model.Status
import kotlinx.coroutines.flow.SharedFlow

interface AdvertiserHandler {
    fun status(): SharedFlow<Status>

    fun reset()
}
