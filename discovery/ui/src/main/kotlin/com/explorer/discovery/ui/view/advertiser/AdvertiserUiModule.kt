package com.explorer.discovery.ui.view.advertiser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.explorer.core.ui.base.UiViewModel
import com.explorer.discovery.ui.viewmodel.AdvertiserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AdvertiserUiModule {
    @Binds
    @AdvertiserUi.Scope
    fun factory(factory: UiViewModel.Factory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @AdvertiserUi.Scope
    @UiViewModel.Bind(AdvertiserViewModel::class)
    fun viewModel(viewModel: AdvertiserViewModel): ViewModel
}
