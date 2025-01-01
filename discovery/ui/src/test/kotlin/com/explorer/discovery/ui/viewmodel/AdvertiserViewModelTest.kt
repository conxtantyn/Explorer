package com.explorer.discovery.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.usecase.advertiser.ConnectedDevicesUsecase
import com.explorer.discovery.domain.usecase.advertiser.AdvertStatusUsecase
import com.explorer.discovery.domain.usecase.advertiser.AdvertiseUsecase
import com.explorer.discovery.domain.usecase.advertiser.CancelAdvertiserUsecase
import com.explorer.discovery.ui.mapper.mapToUi
import com.explorer.discovery.ui.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AdvertiserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val advertiseUsecase: AdvertiseUsecase = mockk(relaxed = true)
    private val cancelAdvertiserUsecase: CancelAdvertiserUsecase = mockk(relaxed = true)
    private val connectedDevicesFlow = mutableListOf<Device>()
    private val discoveryStatusFlow = MutableSharedFlow<Status>(replay = 1)
    private val connectedDevicesUsecase: ConnectedDevicesUsecase = mockk {
        every { this@mockk() } returns connectedDevicesFlow
    }
    private val advertStatusUsecase: AdvertStatusUsecase = mockk {
        every { this@mockk() } returns discoveryStatusFlow
    }

    private lateinit var viewModel: AdvertiserViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val observer: Observer<AdvertiserViewModel.State> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AdvertiserViewModel(
            advertiseUsecase,
            connectedDevicesUsecase,
            advertStatusUsecase,
            cancelAdvertiserUsecase
        )
        viewModel.state.observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state updates correctly when status is emitted`() = runTest {
        val testStatus = Status.Active

        discoveryStatusFlow.emit(testStatus)

        verify { observer.onChanged(AdvertiserViewModel.State(loading = true)) }
    }

    @Test
    fun `start() updates correctly when status is emitted`() = runTest {
        val name = "<test-name>"

        viewModel.start(name)

        verify {
            advertiseUsecase(AdvertiseUsecase.Param(BuildConfig.UUID, name))
        }
    }

    @Test
    fun `state updates correctly when stopDiscoveryUsecase is called`() = runTest {
        viewModel.stop()
        verify { cancelAdvertiserUsecase() }
    }

    @Test
    fun `devices() correctly emits devices`() = runTest {
        val device = Device("Device1", "00:11:22:33:44:55")

        connectedDevicesFlow.add(device)
        discoveryStatusFlow.emit(Status.Idle)
        viewModel.devices()

        verify { connectedDevicesUsecase() }
        verify { observer.onChanged(AdvertiserViewModel.State(devices = listOf(device.mapToUi()))) }
    }
}
