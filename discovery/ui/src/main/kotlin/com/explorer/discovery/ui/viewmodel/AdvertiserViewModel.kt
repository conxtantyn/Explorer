package com.explorer.discovery.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.usecase.advertiser.ConnectedDevicesUsecase
import com.explorer.discovery.domain.usecase.advertiser.AdvertStatusUsecase
import com.explorer.discovery.domain.usecase.advertiser.AdvertiseUsecase
import com.explorer.discovery.domain.usecase.advertiser.CancelAdvertiserUsecase
import com.explorer.discovery.ui.mapper.mapToUi
import com.explorer.discovery.ui.BuildConfig
import com.explorer.discovery.ui.model.UiDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdvertiserViewModel @Inject constructor(
    private val advertiseUsecase: AdvertiseUsecase,
    private val connectedDevicesUsecase: ConnectedDevicesUsecase,
    private val advertStatusUsecase: AdvertStatusUsecase,
    private val cancelAdvertiserUsecase: CancelAdvertiserUsecase,
) : ViewModel() {
    private val devices = MutableStateFlow<List<UiDevice>>(emptyList())

    private val mutableState = MutableLiveData<State>()

    val state: LiveData<State> = mutableState

    init {
        viewModelScope.launch {
            combine(
                devices,
                advertStatusUsecase(),
            ) { devices, status ->
                State(
                    loading = status is Status.Active,
                    devices = devices,
                    error = (status as? Status.Error?)?.error
                )
            }.collectLatest(mutableState::postValue)
        }
    }

    fun start(name: String) {
        viewModelScope.launch {
            advertiseUsecase(AdvertiseUsecase.Param(BuildConfig.UUID, name))
        }
    }

    fun devices() {
        viewModelScope.launch { devices.tryEmit(connectedDevicesUsecase().map(Device::mapToUi)) }
    }

    fun stop() {
        viewModelScope.launch { cancelAdvertiserUsecase() }
    }

    data class State(
        val loading: Boolean = false,
        val devices: List<UiDevice> = listOf(),
        val error: Throwable? = null
    )
}
