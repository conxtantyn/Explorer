package com.explorer.android.module

import com.explorer.discovery.data.datasource.AdvertiserDatasource
import com.explorer.discovery.data.handler.AdvertiserHandler
import com.explorer.discovery.data.handler.AdvertiserHandlerDelegate
import com.explorer.discovery.data.repository.AdvertiserRepositoryDelegate
import com.explorer.discovery.domain.repository.AdvertiserRepository
import dagger.Binds
import dagger.Module

@Module
interface AdvertiserModule {
    @Binds
    fun advertiserHandler(delegate: AdvertiserHandlerDelegate): AdvertiserHandler

    @Binds
    fun advertiserListener(delegate: AdvertiserHandlerDelegate): AdvertiserDatasource.Listener

    @Binds
    fun advertiserRepository(delegate: AdvertiserRepositoryDelegate): AdvertiserRepository
}
