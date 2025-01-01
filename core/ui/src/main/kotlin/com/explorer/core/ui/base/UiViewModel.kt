package com.explorer.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.explorer.core.ui.exception.UiViewModelException
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

interface UiViewModel {
    fun viewModelFactory(): ViewModelProvider.Factory

    @MapKey
    annotation class Bind(val key: KClass<out ViewModel>)

    class Factory @Inject constructor(
        private val classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return classToViewModel[modelClass]?.get() as? T
                ?: throw UiViewModelException(modelClass.name)
        }
    }
}
