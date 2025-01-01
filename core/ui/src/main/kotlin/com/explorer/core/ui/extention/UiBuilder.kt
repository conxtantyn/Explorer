package com.explorer.core.ui.extention

import com.explorer.core.ui.base.UiBuilder

fun<B : UiBuilder> UiBuilder.FactoryProvider.builder(clazz: Class<B>): B {
    return factory().builder(clazz)
}
