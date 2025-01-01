package com.explorer.core.ui.base

import com.explorer.core.ui.view.empty.EmptyUiBuilder
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class  UiModule {
    @Binds
    abstract fun bindFactory(factory: UiBuilder.FactoryDelegate): UiBuilder.Factory

    @Binds
    @IntoMap
    @UiBuilder.Bind(EmptyUiBuilder::class)
    internal abstract fun bindEmptyUiBuilder(builder: EmptyUiBuilder): UiBuilder
}
