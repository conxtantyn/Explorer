package com.explorer.discovery.domain.usecase.advertiser

import com.explorer.core.common.usecase.SyncUsecase
import com.explorer.discovery.domain.repository.AdvertiserRepository
import javax.inject.Inject

class CancelAdvertiserUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : SyncUsecase<Unit> {
    override fun invoke() {
        repository.stop()
    }
}
