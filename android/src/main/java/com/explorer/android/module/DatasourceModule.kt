package com.explorer.android.module

import com.explorer.discovery.data.datasource.AdvertiserDatasource
import com.explorer.discovery.data.datasource.ConsumerDatasource
import com.explorer.discovery.local.datasource.AdvertiserDatasourceDelegate
import com.explorer.discovery.local.datasource.ConsumerDatasourceDelegate
import dagger.Binds
import dagger.Module

@Module
interface DatasourceModule {
    @Binds
    fun discoveryDatasource(delegate: AdvertiserDatasourceDelegate): AdvertiserDatasource

    @Binds
    fun searchDatasource(delegate: ConsumerDatasourceDelegate): ConsumerDatasource
}
