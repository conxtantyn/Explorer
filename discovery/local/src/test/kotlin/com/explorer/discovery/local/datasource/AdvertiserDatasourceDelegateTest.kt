package com.explorer.discovery.local.datasource

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.os.ParcelUuid
import com.explorer.discovery.data.datasource.AdvertiserDatasource
import com.explorer.discovery.local.factory.AdvertiserFactory
import com.explorer.discovery.local.mapper.mapToDomain
import com.explorer.discovery.local.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class AdvertiserDatasourceDelegateTest {
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothAdvertiser: BluetoothLeAdvertiser
    private lateinit var adapter: AdvertiserDatasource
    private lateinit var listener: AdvertiserDatasource.Listener
    private lateinit var settings: AdvertiseSettings.Builder
    private lateinit var callback: AdvertiseCallback
    private lateinit var data: AdvertiseData.Builder

    @Before
    fun setup() {
        bluetoothManager = mockk()
        bluetoothAdapter = mockk()
        bluetoothAdvertiser = mockk()
        listener = mockk()
        settings = mockk(relaxed = true)
        data = mockk(relaxed = true)

        every { bluetoothManager.adapter } returns bluetoothAdapter
        every { bluetoothAdapter.bluetoothLeAdvertiser } returns bluetoothAdvertiser

        adapter = AdvertiserDatasourceDelegate(
            bluetoothManager,
            object : AdvertiserFactory {
                override fun settings(): AdvertiseSettings.Builder = settings
                override fun data(): AdvertiseData.Builder = data
            },
            listener
        ).also { callback = it }
    }

    @Test
    fun `activate() should start advertising`() {
        val name = "<test-name>"
        val identifier = "0000180A-0000-1000-8000-00805F9B34FB"
        val mockData = mockk<AdvertiseData>()

        every { bluetoothAdvertiser.startAdvertising(any(), any(), any()) } returns Unit

        every { settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY) } returns settings
        every { settings.setConnectable(BuildConfig.CONNECTABLE) } returns settings
        every { settings.setTimeout(AdvertiserDatasourceDelegate.TIMEOUT) } returns settings
        every { settings.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM) } returns settings

        every { data.setIncludeDeviceName(AdvertiserDatasourceDelegate.INCLUDE_DEVICE_NAME) } returns data
        every { data.addServiceUuid(any<ParcelUuid>()) } returns data
        every { data.addManufacturerData(any(), any()) } returns data
        every { data.build() } returns mockData

        adapter.start(identifier, name)

        verify {
            bluetoothAdvertiser.startAdvertising(any(), any(), eq(callback))

            settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            settings.setConnectable(BuildConfig.CONNECTABLE)
            settings.setTimeout(AdvertiserDatasourceDelegate.TIMEOUT)
            settings.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)

            data.setIncludeDeviceName(AdvertiserDatasourceDelegate.INCLUDE_DEVICE_NAME)
            data.addManufacturerData(BuildConfig.MANUFACTURER, name.toByteArray(Charsets.UTF_8))
            data.addServiceUuid(any<ParcelUuid>())
            data.build()
        }
    }

    @Test
    fun `devices() should return mapped bonded and connected devices`() {
        val mockDevice = mockk<BluetoothDevice>()
        val mockGattDevice = mockk<BluetoothDevice>()
        val bondedDevices = listOf(mockDevice)

        every { mockDevice.name } returns "<test-device-name>"
        every { mockDevice.address } returns "<test-device-address>"
        every { mockGattDevice.name } returns "<test-device-name>"
        every { mockGattDevice.address } returns "<test-device-address>"
        every { bluetoothAdapter.bondedDevices } returns bondedDevices.toSet()
        every { bluetoothManager.getConnectedDevices(BluetoothProfile.GATT) } returns listOf(mockGattDevice)

        val result = adapter.devices()

        assertEquals(listOf(mockDevice.mapToDomain(), mockGattDevice.mapToDomain()), result)

        verify { bluetoothAdapter.bondedDevices }
        verify { bluetoothManager.getConnectedDevices(BluetoothProfile.GATT) }
        verify { mockDevice.mapToDomain() }
        verify { mockGattDevice.mapToDomain() }
    }

    @Test
    fun `deactivate() should stop advertising`() {
        every { bluetoothAdvertiser.stopAdvertising(any()) } returns Unit

        adapter.stop()

        verify { bluetoothAdvertiser.stopAdvertising(eq(callback)) }
    }
}
