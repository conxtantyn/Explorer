package com.explorer.discovery.domain.usecase.advertiser

import com.explorer.core.common.usecase.SyncUsecase
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.repository.AdvertiserRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class AdvertStatusUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : SyncUsecase<SharedFlow<Status>> {
    override fun invoke(): SharedFlow<Status> {
        return repository.status()
    }
}
