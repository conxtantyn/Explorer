package com.explorer.android.module

import com.explorer.discovery.local.factory.AdvertiserFactory
import com.explorer.discovery.local.factory.ConsumerFactory
import dagger.Binds
import dagger.Module

@Module
interface FactoryModule {
    @Binds
    fun advertiserFactory(delegate: AdvertiserFactory.Delegate): AdvertiserFactory

    @Binds
    fun scannerFactory(delegate: ConsumerFactory.Delegate): ConsumerFactory
}
