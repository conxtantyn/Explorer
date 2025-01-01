package com.explorer.discovery.ui.view.advertiser

import com.explorer.core.ui.base.UiComponent
import com.explorer.core.ui.base.UiModule
import com.explorer.core.ui.base.UiViewModel
import com.explorer.discovery.domain.repository.AdvertiserRepository
import dagger.BindsInstance

interface AdvertiserUi {
    fun advertiser(): AdvertiserRepository

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ AdvertiserUi::class ],
        modules = [ UiModule::class, AdvertiserUiModule::class ]
    )
    interface Component : AdvertiserUi, UiComponent, UiViewModel {
        @dagger.Component.Builder
        interface Builder {
            fun advertiserUi(advertiserUi: AdvertiserUi): Builder

            @BindsInstance
            fun param(param: AdvertiserUiParam): Builder

            fun build(): Component
        }

        fun param(): AdvertiserUiParam
    }
}
