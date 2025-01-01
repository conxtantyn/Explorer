package com.explorer.discovery.data.handler

import com.explorer.discovery.data.datasource.AdvertiserDatasource
import com.explorer.discovery.data.exception.AdvertiserException
import com.explorer.discovery.domain.model.Status
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdvertiserHandlerDelegate @Inject constructor() : AdvertiserHandler, AdvertiserDatasource.Listener {
    private val status = MutableSharedFlow<Status>(replay = RELAY).apply { tryEmit(Status.Idle) }

    override fun onStart() {
        status.tryEmit(Status.Active)
    }

    override fun status(): SharedFlow<Status> = status

    override fun onError(code: Int) {
        status.tryEmit(Status.Error(AdvertiserException(code)))
    }

    override fun reset() {
        status.tryEmit(Status.Idle)
    }

    private companion object {
        const val RELAY = 1
    }
}
