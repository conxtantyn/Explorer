package com.explorer.android

import android.bluetooth.BluetoothManager
import android.content.Context
import com.explorer.android.ui.main.MainUi
import com.explorer.android.ui.main.MainUiBuilder
import com.explorer.core.ui.base.UiBuilder
import com.explorer.core.ui.base.UiComponent
import com.explorer.core.ui.base.UiModule
import com.explorer.android.module.AdvertiserModule
import com.explorer.android.module.ConsumerModule
import com.explorer.android.module.DatasourceModule
import com.explorer.android.module.FactoryModule
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

interface Explorer {
    fun getApplicationContext(): Context

    @Module
    object Provider {
        @Singleton
        @Provides
        @IntoMap
        @UiBuilder.Bind(MainUiBuilder::class)
        fun provideMainBuilder(component: Component): UiBuilder {
            return MainUiBuilder(component)
        }

        @Provides
        fun bluetoothManager(context: Context): BluetoothManager {
            return context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }
    }

    @Singleton
    @dagger.Component(
        dependencies = [ Explorer::class ],
        modules = [
            AdvertiserModule::class,
            ConsumerModule::class,
            FactoryModule::class,
            DatasourceModule::class,
            UiModule::class,
            Provider::class,
        ]
    )
    interface Component : Explorer, MainUi, UiComponent.Android<ExplorerApplication>
}
