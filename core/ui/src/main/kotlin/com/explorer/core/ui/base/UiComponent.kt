package com.explorer.core.ui.base

import dagger.android.AndroidInjector

interface UiComponent : UiBuilder.FactoryProvider {
    interface Provider<T> {
        val injector: T
    }

    interface Android<T> : UiComponent, AndroidInjector<T>
}
