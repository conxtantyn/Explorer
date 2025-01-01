package com.explorer.discovery.ui.view.consumer

import android.content.Context
import com.explorer.core.ui.base.UiBuilder

class ConsumerUiBuilder(
    private val dependency: ConsumerUi
) : UiBuilder.Component<ConsumerUi, ConsumerUi.Component>() {
    override fun build(context: Context): ConsumerUi.Component {
        return DaggerConsumerUi_Component.builder().consumerUi(dependency).build()
    }
}
