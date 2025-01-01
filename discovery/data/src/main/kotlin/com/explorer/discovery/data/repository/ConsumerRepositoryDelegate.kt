package com.explorer.discovery.data.repository

import com.explorer.discovery.data.datasource.ConsumerDatasource
import com.explorer.discovery.data.handler.ConsumerHandler
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.repository.ConsumerRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ConsumerRepositoryDelegate @Inject constructor(
    private val handler: ConsumerHandler,
    private val datasource: ConsumerDatasource,
) : ConsumerRepository {
    override fun start(identifier: String) {
        handler.onStart()
        datasource.start(identifier)
    }

    override fun status(): SharedFlow<Status> = handler.status()

    override fun devices(): SharedFlow<List<Device>> = handler.devices()

    override fun stop() {
        handler.reset()
        datasource.stop()
    }
}
