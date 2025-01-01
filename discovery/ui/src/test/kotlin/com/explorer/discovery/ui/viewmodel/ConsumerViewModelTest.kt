package com.explorer.discovery.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.usecase.search.NearbyDevicesUsecase
import com.explorer.discovery.domain.usecase.search.ConsumerStatusUsecase
import com.explorer.discovery.domain.usecase.search.ConsumerUsecase
import com.explorer.discovery.domain.usecase.search.StopConsumerUsecase
import com.explorer.discovery.ui.BuildConfig
import com.explorer.discovery.ui.mapper.mapToUi
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ConsumerViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val nearbyDevicesFlow = MutableSharedFlow<List<Device>>()
    private val searchStatusFlow = MutableSharedFlow<Status>()

    private val consumerUsecase: ConsumerUsecase = mockk(relaxed = true)
    private val stopConsumerUsecase: StopConsumerUsecase = mockk(relaxed = true)
    private val nearbyDevicesUsecase: NearbyDevicesUsecase = mockk {
        every { this@mockk() } returns nearbyDevicesFlow
    }
    private val consumerStatusUsecase: ConsumerStatusUsecase = mockk {
        every { this@mockk() } returns searchStatusFlow
    }

    private lateinit var viewModel: ConsumerViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ConsumerViewModel(
            consumerUsecase,
            nearbyDevicesUsecase,
            consumerStatusUsecase,
            stopConsumerUsecase
        )
    }

    @Test
    fun `state emits combined devices and status`() = runTest {
        val status = Status.Active
        val devices = listOf(
            Device("Device1", "00:11:22:33:44:55"),
            Device("Device2", "00:11:22:33:44:55")
        )

        nearbyDevicesFlow.emit(devices)
        searchStatusFlow.emit(status)

        val observer: Observer<ConsumerViewModel.State> = mockk(relaxed = true)
        viewModel.state.observeForever(observer)

        testScheduler.advanceUntilIdle()

        val expectedState = ConsumerViewModel.State(
            loading = true,
            devices = devices.map(Device::mapToUi),
            error = null
        )
        verify { observer.onChanged(expectedState) }
    }

    @Test
    fun `start invokes StartSearchUsecase`() = runTest {
        viewModel.start()

        coVerify { consumerUsecase(BuildConfig.UUID) }
    }

    @Test
    fun `stop invokes StopSearchUsecase`() = runTest {
        viewModel.stop()
        coVerify { stopConsumerUsecase() }
    }

    @Test
    fun `error in flows updates state with error`() = runTest {
        val error = RuntimeException("Test Error")

        nearbyDevicesFlow.emit(listOf())
        searchStatusFlow.emit(Status.Error(error))

        val observer: Observer<ConsumerViewModel.State> = mockk(relaxed = true)

        viewModel.state.observeForever(observer)
        testScheduler.advanceUntilIdle()

        val expectedState = ConsumerViewModel.State(
            loading = false,
            devices = listOf(),
            error = error
        )
        verify { observer.onChanged(expectedState) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
