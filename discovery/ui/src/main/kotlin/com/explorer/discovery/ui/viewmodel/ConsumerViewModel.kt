package com.explorer.discovery.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.usecase.search.NearbyDevicesUsecase
import com.explorer.discovery.domain.usecase.search.ConsumerStatusUsecase
import com.explorer.discovery.domain.usecase.search.ConsumerUsecase
import com.explorer.discovery.domain.usecase.search.StopConsumerUsecase
import com.explorer.discovery.ui.mapper.mapToUi
import com.explorer.discovery.ui.BuildConfig
import com.explorer.discovery.ui.model.UiDevice
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConsumerViewModel @Inject constructor(
    private val consumerUsecase: ConsumerUsecase,
    private val nearbyDevicesUsecase: NearbyDevicesUsecase,
    private val consumerStatusUsecase: ConsumerStatusUsecase,
    private val stopConsumerUsecase: StopConsumerUsecase,
) : ViewModel() {

    private val mutableState = MutableLiveData(State())

    val state: LiveData<State> = mutableState

    init {
        viewModelScope.launch {
            combine(
                nearbyDevicesUsecase(),
                consumerStatusUsecase(),
            ) { devices, status ->
                State(
                    loading = status is Status.Active,
                    devices = devices.map(Device::mapToUi),
                    error = (status as? Status.Error)?.error
                )
            }.collectLatest { state ->
                mutableState.postValue(state)
            }
        }
    }

    fun start() {
        viewModelScope.launch { consumerUsecase(BuildConfig.UUID) }
    }

    fun stop() {
        viewModelScope.launch { stopConsumerUsecase() }
    }

    data class State(
        val loading: Boolean = false,
        val devices: List<UiDevice> = listOf(),
        val error: Throwable? = null
    )
}
