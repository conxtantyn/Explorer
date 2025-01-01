package com.explorer.core.ui.view.empty

import android.content.Context
import com.explorer.core.ui.base.UiBuilder
import javax.inject.Inject

class EmptyUiBuilder @Inject constructor() : UiBuilder.Component<EmptyUi, EmptyUi.Component>() {
    override fun build(context: Context): EmptyUi.Component {
        return DaggerEmptyUi_Component.builder().build()
    }
}
