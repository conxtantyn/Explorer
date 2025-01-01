package com.explorer.discovery.ui.view.discovery

import com.explorer.core.ui.base.UiComponent
import com.explorer.core.ui.base.UiModule
import com.explorer.core.ui.base.UiViewModel
import com.explorer.discovery.domain.repository.AdvertiserRepository
import com.explorer.discovery.domain.repository.ConsumerRepository
import com.explorer.discovery.ui.view.advertiser.AdvertiserUi
import com.explorer.discovery.ui.view.consumer.ConsumerUi
import dagger.BindsInstance

interface DiscoveryUi {
    fun advertiser(): AdvertiserRepository

    fun consumer(): ConsumerRepository

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ DiscoveryUi::class ],
        modules = [ UiModule::class, DiscoveryUiModule::class ]
    )
    interface Component : DiscoveryUi,
        UiComponent,
        AdvertiserUi,
        ConsumerUi,
        UiViewModel {
        @dagger.Component.Builder
        interface Builder {
            fun discoveryUi(discoveryUi: DiscoveryUi): Builder

            @BindsInstance
            fun param(param: DiscoveryUiParam): Builder

            fun build(): Component
        }

        fun param(): DiscoveryUiParam
    }
}
