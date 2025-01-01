package com.explorer.discovery.domain.usecase.search

import com.explorer.core.common.usecase.SyncUsecase
import com.explorer.discovery.domain.repository.ConsumerRepository
import javax.inject.Inject

class StopConsumerUsecase @Inject constructor(
    private val repository: ConsumerRepository
) : SyncUsecase<Unit> {
    override fun invoke() {
        repository.stop()
    }
}
