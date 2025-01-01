package com.explorer.android

import android.app.Application
import com.explorer.core.ui.base.UiComponent

class ExplorerApplication : Application(), Explorer, UiComponent.Provider<Explorer.Component> {
    override val injector: Explorer.Component by lazy {
        ExplorerBuilder(this).build(this)
    }
}
