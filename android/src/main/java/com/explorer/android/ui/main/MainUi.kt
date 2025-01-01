package com.explorer.android.ui.main

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.explorer.core.ui.base.UiBuilder
import com.explorer.core.ui.base.UiComponent
import com.explorer.core.ui.base.UiModule
import com.explorer.core.ui.base.UiViewModel
import com.explorer.discovery.domain.repository.AdvertiserRepository
import com.explorer.discovery.domain.repository.ConsumerRepository
import com.explorer.discovery.ui.view.discovery.DiscoveryUi
import com.explorer.discovery.ui.view.discovery.DiscoveryUiBuilder
import dagger.Binds
import dagger.Provides
import dagger.multibindings.IntoMap

interface MainUi {
    fun context(): Context

    fun advertiser(): AdvertiserRepository

    fun consumer(): ConsumerRepository

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @dagger.Module
    interface Module {
        @Binds
        @Scope
        fun factory(factory: UiViewModel.Factory): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @Scope
        @UiViewModel.Bind(MainUiViewModel::class)
        fun viewModel(viewModel: MainUiViewModel): ViewModel
    }

    @dagger.Module
    object Provider {
        @Scope
        @Provides
        fun providePermissions(): List<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            } else {
                listOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        @Scope
        @Provides
        @IntoMap
        @UiBuilder.Bind(DiscoveryUiBuilder::class)
        fun provideDiscoveryUiBuilder(component: Component): UiBuilder {
            return DiscoveryUiBuilder(component)
        }
    }

    @Scope
    @dagger.Component(
        dependencies = [ MainUi::class ],
        modules = [
            UiModule::class,
            Module::class,
            Provider::class,
        ]
    )
    interface Component : MainUi, UiComponent.Android<MainUiActivity>, DiscoveryUi, UiViewModel
}
