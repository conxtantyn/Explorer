package com.explorer.discovery.domain.usecase.search

import com.explorer.core.common.usecase.SyncUsecase
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.repository.ConsumerRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ConsumerStatusUsecase @Inject constructor(
    private val repository: ConsumerRepository
) : SyncUsecase<SharedFlow<Status>> {
    override fun invoke(): SharedFlow<Status> {
        return repository.status()
    }
}
