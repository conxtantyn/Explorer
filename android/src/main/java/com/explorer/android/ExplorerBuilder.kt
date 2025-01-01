package com.explorer.android

import android.content.Context
import com.explorer.core.ui.base.UiBuilder

class ExplorerBuilder(
    private val dependency: Explorer
) : UiBuilder.Component<Explorer, Explorer.Component>() {
    override fun build(context: Context): Explorer.Component {
        return DaggerExplorer_Component.builder().explorer(dependency).build()
    }
}
