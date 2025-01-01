package com.explorer.discovery.ui.view.consumer

import com.explorer.core.ui.base.UiComponent
import com.explorer.core.ui.base.UiModule
import com.explorer.core.ui.base.UiViewModel
import com.explorer.discovery.domain.repository.ConsumerRepository

interface ConsumerUi {
    fun consumer(): ConsumerRepository

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ ConsumerUi::class ],
        modules = [ UiModule::class, ConsumerUiModule::class ]
    )
    interface Component : ConsumerUi, UiComponent, UiViewModel
}
