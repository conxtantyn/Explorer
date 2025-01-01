package com.explorer.discovery.ui.view.discovery

import android.content.Context
import com.explorer.core.ui.base.UiBuilder

class DiscoveryUiBuilder(
    private val dependency: DiscoveryUi
) : UiBuilder.ComponentWithParam<DiscoveryUiParam, DiscoveryUi, DiscoveryUi.Component>() {
    override fun build(context: Context, param: DiscoveryUiParam): DiscoveryUi.Component {
        return DaggerDiscoveryUi_Component.builder().discoveryUi(dependency).param(param).build()
    }
}
