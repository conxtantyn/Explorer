package com.explorer.discovery.data.repository

import com.explorer.discovery.data.datasource.AdvertiserDatasource
import com.explorer.discovery.data.handler.AdvertiserHandler
import com.explorer.discovery.data.handler.AdvertiserHandlerDelegate
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class AdvertiserRepositoryDelegateTest {
    private lateinit var handler: AdvertiserHandler
    private lateinit var datasource: AdvertiserDatasource
    private lateinit var listener: AdvertiserDatasource.Listener
    private lateinit var repository: AdvertiserRepositoryDelegate

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val discovery = AdvertiserHandlerDelegate()
        handler = discovery
        listener = discovery
        datasource = mockk(relaxed = true)
        every { datasource.start(any(), any()) } answers {
            listener.onStart()
        }
        repository = AdvertiserRepositoryDelegate(handler, datasource)
    }

    @Test
    fun `start() should update status to Active`() = runTest {
        val identifier = "<test-identifier>"
        val name = "<test-name>"

        repository.start(identifier, name)

        val status = repository.status().first()

        assertEquals(Status.Active, status)
        coVerify { datasource.start(identifier, name) }
    }

    @Test
    fun `stop() should update status to Idle`() = runTest {
        repository.start("<test-identifier>", "<test-name>")

        repository.stop()

        val status = repository.status().first()

        assertEquals(Status.Idle, status)
        verify { datasource.stop() }
    }

    @Test
    fun `devices() should return the list of devices from datasource`() {
        val devices = listOf(
            Device("Device 1", "00:11:22:33:44:55"),
            Device("Device 2", "00:11:22:33:44:56")
        )
        every { datasource.devices() } returns devices

        val result = repository.devices()

        assertEquals(devices, result)
        verify { datasource.devices() }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
