package com.explorer.discovery.domain.usecase.advertiser

import com.explorer.core.common.usecase.SyncUsecase
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.repository.AdvertiserRepository
import javax.inject.Inject

class ConnectedDevicesUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : SyncUsecase<List<Device>> {
    override fun invoke(): List<Device> {
        return repository.devices()
    }
}
