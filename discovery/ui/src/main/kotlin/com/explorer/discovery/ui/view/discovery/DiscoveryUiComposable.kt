package com.explorer.discovery.ui.view.discovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import com.explorer.core.ui.base.UiBuilder
import com.explorer.core.ui.extention.builder
import com.explorer.discovery.ui.view.advertiser.AdvertiserUiComposable
import com.explorer.discovery.ui.view.consumer.ConsumerUiComposable

@Composable
fun DiscoveryUiComposable(
    identifier: String?,
    factory: UiBuilder.FactoryProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember(identifier) {
        factory.builder(DiscoveryUiBuilder::class.java)
            .build(context, DiscoveryUiParam(identifier))
    }
    if (identifier == null) {
        ConsumerUiComposable(component, viewModelStoreOwner)
    } else {
        AdvertiserUiComposable(
            identifier,
            component,
            viewModelStoreOwner
        )
    }
}
