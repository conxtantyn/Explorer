package com.explorer.core.ui.base

import android.content.Context
import android.os.Parcelable
import com.explorer.core.ui.exception.UiBuilderException
import dagger.MapKey
import javax.inject.Inject
import kotlin.reflect.KClass

interface UiBuilder {
    @MapKey
    annotation class Bind(val key: KClass<out UiBuilder>)

    interface Factory {
        fun <T : UiBuilder> builder(clazz: Class<T>): T
    }

    interface FactoryProvider {
        fun factory(): Factory
    }

    abstract class Component<P, C : P> : UiBuilder {
        abstract fun build(context: Context): C
    }

    abstract class ComponentWithParam<A : Parcelable, P, C : P> : UiBuilder {
        abstract fun build(context: Context, param: A): C
    }

    class FactoryDelegate @Inject constructor(
        private val factory: Map<Class<out UiBuilder>,
                @JvmSuppressWildcards javax.inject.Provider<UiBuilder>>
    ) : Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : UiBuilder> builder(clazz: Class<T>): T {
            return (factory[clazz]?.get() as? T?) ?: throw UiBuilderException(clazz.name)
        }
    }
}
