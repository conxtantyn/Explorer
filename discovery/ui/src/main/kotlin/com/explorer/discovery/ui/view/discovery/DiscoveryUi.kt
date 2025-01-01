package com.explorer.discovery.ui.view.discovery

import com.explorer.core.ui.base.UiBuilder
import com.explorer.core.ui.base.UiComponent
import com.explorer.core.ui.base.UiModule
import com.explorer.discovery.domain.repository.AdvertiserRepository
import com.explorer.discovery.domain.repository.ConsumerRepository
import com.explorer.discovery.ui.view.advertiser.AdvertiserUi
import com.explorer.discovery.ui.view.advertiser.AdvertiserUiBuilder
import com.explorer.discovery.ui.view.consumer.ConsumerUi
import com.explorer.discovery.ui.view.consumer.ConsumerUiBuilder
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

interface DiscoveryUi {
    fun advertiser(): AdvertiserRepository

    fun consumer(): ConsumerRepository

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Module
    object Provider {
        @Scope
        @Provides
        @IntoMap
        @UiBuilder.Bind(AdvertiserUiBuilder::class)
        fun provideAdvertiserUiBuilder(component: Component): UiBuilder {
            return AdvertiserUiBuilder(component)
        }

        @Scope
        @Provides
        @IntoMap
        @UiBuilder.Bind(ConsumerUiBuilder::class)
        fun provideConsumerUiBuilder(component: Component): UiBuilder {
            return ConsumerUiBuilder(component)
        }
    }

    @Scope
    @dagger.Component(
        dependencies = [ DiscoveryUi::class ],
        modules = [ UiModule::class, Provider::class ]
    )
    interface Component : DiscoveryUi,
        UiComponent,
        AdvertiserUi,
        ConsumerUi {
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
