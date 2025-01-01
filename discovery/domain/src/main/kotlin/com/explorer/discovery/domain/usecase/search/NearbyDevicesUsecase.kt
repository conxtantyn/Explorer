package com.explorer.discovery.domain.usecase.search

import com.explorer.core.common.usecase.SyncUsecase
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.repository.ConsumerRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class NearbyDevicesUsecase @Inject constructor(
    private val repository: ConsumerRepository
) : SyncUsecase<SharedFlow<List<Device>>> {
    override fun invoke(): SharedFlow<List<Device>> {
        return repository.devices()
    }
}
