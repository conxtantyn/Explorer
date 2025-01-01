package com.explorer.discovery.data.repository

import com.explorer.discovery.data.datasource.ConsumerDatasource
import com.explorer.discovery.data.handler.ConsumerHandler
import com.explorer.discovery.data.handler.ConsumerHandlerDelegate
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.domain.model.Status
import com.explorer.discovery.domain.repository.ConsumerRepository
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ConsumerRepositoryDelegateTest {
    private lateinit var handler: ConsumerHandler
    private lateinit var datasource: ConsumerDatasource
    private lateinit var repository: ConsumerRepository
    private lateinit var listener: ConsumerDatasource.Listener

    @Before
    fun setup() {
        val delegate = ConsumerHandlerDelegate()
        handler = delegate
        listener = delegate
        datasource = mockk(relaxed = true)
        repository = ConsumerRepositoryDelegate(handler, datasource)
    }

    @Test
    fun `start() should set status to Active and start the datasource`() = runTest {
        val identifier = "<test-identifier>"
        repository.start(identifier)

        val status = repository.status().first()
        assertEquals(Status.Active, status)

        verify { datasource.start(identifier) }
    }

    @Test
    fun `onDiscover() should add a new device and emit updated device list`() = runTest {
        val device = Device(name = "Device1", address = "00:11:22:33:44:55")
        listener.onDiscover(device)

        val devices = repository.devices().first()
        assertTrue(devices.contains(device))
    }

    @Test
    fun `onDiscover() should not emit duplicate devices`() = runTest {
        val device = Device(name = "Device1", address = "00:11:22:33:44:55")
        listener.onDiscover(device)
        listener.onDiscover(device)

        val devices = repository.devices().first()
        assertEquals(1, devices.size)
    }

    @Test
    fun `stop() should clear devices, stop datasource, and set status to Idle`() = runTest {
        val device = Device(name = "Device1", address = "00:11:22:33:44:55")
        listener.onDiscover(device)

        repository.stop()

        val devices = repository.devices().first()
        val status = repository.status().first()

        assertTrue(devices.isEmpty())
        assertEquals(Status.Idle, status)
        verify { datasource.stop() }
    }
}
