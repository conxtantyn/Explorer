package com.explorer.android.module

import com.explorer.discovery.data.datasource.ConsumerDatasource
import com.explorer.discovery.data.handler.ConsumerHandler
import com.explorer.discovery.data.handler.ConsumerHandlerDelegate
import com.explorer.discovery.data.repository.ConsumerRepositoryDelegate
import com.explorer.discovery.domain.repository.ConsumerRepository
import dagger.Binds
import dagger.Module

@Module
interface ConsumerModule {
    @Binds
    fun consumerHandler(delegate: ConsumerHandlerDelegate): ConsumerHandler

    @Binds
    fun consumerListener(delegate: ConsumerHandlerDelegate): ConsumerDatasource.Listener

    @Binds
    fun consumerRepository(delegate: ConsumerRepositoryDelegate): ConsumerRepository
}
